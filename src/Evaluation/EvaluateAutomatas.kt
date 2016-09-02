/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package EvaluateUtility

import TheGlobalAutomata.*
import MainWindowUtility.*
import OperationsAndActions.*

import java.awt.*
import java.util.*
import javax.swing.*

class EvaluateAutomatas{

    fun evaluate(c: Container, m: GlobalAutomata){
        val stringToEvaluate = MainUtility().getComponentByName<JTextField>(c, "t7StringTextField")
        var result = false
        if(stringToEvaluate != null) {
            val currentAutomataType = m.globalAutomataType
            when (currentAutomataType) {
                "DFA" -> result = evaluateDFA(stringToEvaluate.text,m)
                "NFA" -> result = evaluateNFA(stringToEvaluate.text,m)
                "NFA-E" -> result = evaluateNFAE(stringToEvaluate.text,m)
                "PDA" -> result = evaluatePDA(stringToEvaluate.text,m)
            }
        }

        val resultLabel = MainUtility().getComponentByName<JLabel>(c, "t7ResultLabel")
        if(resultLabel != null){
            if(result){
                resultLabel.foreground = java.awt.Color.decode("#02A345")
                resultLabel.text = "SUCCESS"
            }else{
                resultLabel.foreground = java.awt.Color.decode("#FF0000")
                resultLabel.text = "FAILURE"
            }
        }
    }

    fun evaluateDFA(wToEvaluate: String, m: GlobalAutomata): Boolean {
        var result = false
        var currentIteratorState = m.globalInitialState

        for(i in 0..(wToEvaluate.length-1)){

            var iterated = false

            for(j in 0..(m.globalDeltas.size-1)){
                if(!iterated) {
                    val currentDelta = m.globalDeltas.get(j)
                    val deltaData1 = currentDelta.split('(').get(1).split(')')
                    val deltaData2 = deltaData1.get(0).split(',')
                    val deltaData3 = deltaData1.get(1).split('=').get(1)

                    if ((deltaData2.get(0).equals(currentIteratorState)) && (deltaData2.get(1).equals(wToEvaluate.get(i).toString()))) {
                        currentIteratorState = deltaData3
                        iterated = true
                    }
                }
            }

            if(!iterated){
                result = false
                break
            }

            if(i == (wToEvaluate.length-1)){
                for(k in 0..(m.globalAcceptanceStates.size-1)){
                    if(m.globalAcceptanceStates.get(k).equals(currentIteratorState)){
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun evaluateNFA(wToEvaluate: String, m:GlobalAutomata): Boolean{
        var nfaInDfa = AutomataOperationsAndActions().NFA_To_DFA(m)
        return evaluateDFA(wToEvaluate,nfaInDfa)
    }

    fun evaluateNFAE(wToEvaluate: String, m: GlobalAutomata): Boolean{
        var nfaeInNfa = AutomataOperationsAndActions().NFAE_To_NFA(m)
        return evaluateNFA(wToEvaluate,nfaeInNfa)
    }

    fun evaluatePDA(wToEvaluate: String, m: GlobalAutomata): Boolean{
        var result = false

        var wToEval = wToEvaluate
        if(wToEval==""){
            wToEval = "E"
        }

        var leafNodes = ArrayList<String>()

        var firstStateToProcess = m.globalInitialState+"@"+wToEval+"@"+m.globalInitialStatePile
        var statesToProcess = ArrayList<String>()
        var tempStatesToProcess = ArrayList<String>()
        statesToProcess.add(firstStateToProcess)

        var deltaToSearch1 = ""
        var deltaResult1 = ""

        var deltaResult2 = ""
        var deltaToSearch2 = ""

        var newState = ""
        var tempW = ""
        var newW = ""
        var deltaResultPilePart = ""
        var newPile = ""

        while(statesToProcess.size > 0){

            tempStatesToProcess.clear()

            for(i in 0..(statesToProcess.size-1)){

                var currentStateParts = statesToProcess.get(i).split('@')

                if(!currentStateParts.get(1).equals("E")){
                    deltaToSearch1 = "delta("+currentStateParts.get(0)+","+currentStateParts.get(1).get(0)+","+currentStateParts.get(2)+")"
                    deltaToSearch2 = "delta("+currentStateParts.get(0)+",E,"+currentStateParts.get(2)+")"
                } else {
                    deltaToSearch1 = ""
                    deltaToSearch2 = "delta("+currentStateParts.get(0)+",E,"+currentStateParts.get(2)+")"
                }

                deltaResult1 = ""
                deltaResult2 = ""
                for(d in 0..(m.globalDeltas.size-1)){
                    if(m.globalDeltas.get(d).contains(deltaToSearch1)){
                        deltaResult1 = m.globalDeltas.get(d).split('=').get(1)
                    }
                    if(m.globalDeltas.get(d).contains(deltaToSearch2)){
                        deltaResult2 = m.globalDeltas.get(d).split('=').get(1)
                    }
                }

                if(deltaResult1.equals("") && deltaResult2.equals("")){
                    leafNodes.add(statesToProcess.get(i))
                } else {

                    //ver deltaResult1
                    if(!deltaResult1.equals("")){

                        newState = deltaResult1.split('(').get(1).split(',').get(0)

                        tempW = currentStateParts.get(1)
                        newW = tempW.substring(1,tempW.length)
                        if(tempW.length==1){
                            newW="E"
                        }

                        //La pila queda igual
                        newPile = currentStateParts.get(2)

                        deltaResultPilePart = deltaResult1.split(',').get(1).split(')').get(0)

                        //Pop
                        if(deltaResultPilePart.equals("E")){
                            newPile = currentStateParts.get(2).substring(0,currentStateParts.get(2).length-1)
                        }
                        //Push
                        if(deltaResultPilePart.length>1){
                            newPile = currentStateParts.get(2) + deltaResultPilePart.get(0)
                        }
                        tempStatesToProcess.add(newState+"@"+newW+"@"+newPile)
                    }

                    if(!deltaResult2.equals("")){
                        newState = deltaResult1.split('(').get(1).split(',').get(0)

                        newW = currentStateParts.get(1)

                        //La pila queda igual
                        newPile = currentStateParts.get(2)

                        deltaResultPilePart = deltaResult1.split(',').get(1).split(')').get(0)

                        //Pop
                        if(deltaResultPilePart.equals("E")){
                            newPile = currentStateParts.get(2).substring(0,currentStateParts.get(2).length-1)
                        }
                        //Push
                        if(deltaResultPilePart.length>1){
                            newPile = currentStateParts.get(2) + deltaResultPilePart.get(0)
                        }
                        tempStatesToProcess.add(newState+"@"+newW+"@"+newPile)
                    }
                }
            }
            statesToProcess.clear()
            for(k in 0..(tempStatesToProcess.size-1)){
                statesToProcess.add(tempStatesToProcess.get(k))
            }
        }

        var isCurrentStateAcceptance = false
        for(r in 0..(leafNodes.size-1)){
            var currentNode = leafNodes.get(r).split('@')

            isCurrentStateAcceptance = false
            for(a in 0..(m.globalAcceptanceStates.size-1)){
                if(m.globalAcceptanceStates.get(a).equals(currentNode.get(0))){
                    isCurrentStateAcceptance = true
                }
            }

            if(isCurrentStateAcceptance && currentNode.get(1).equals("E")){
                result = true
                break
            }
        }

        return result
    }

}