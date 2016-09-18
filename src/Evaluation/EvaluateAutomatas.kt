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
                "Maquina Turing" -> {
                    var turingResponse = processTuringMachine(stringToEvaluate.text,m)

                    val tapeLabel = MainUtility().getComponentByName<JLabel>(c,"t7TapeLabel")
                    if(tapeLabel != null){
                        tapeLabel.text = "Estado Final De La Cinta : " + turingResponse.get(0)
                    }

                    if(turingResponse.get(1).equals("1")){
                        result = true
                    }
                }
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

        if(wToEvaluate.length==0){
            for(k in 0..(m.globalAcceptanceStates.size-1)){
                if(m.globalAcceptanceStates.get(k).equals(currentIteratorState)){
                    result = true
                }
            }
        }

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
        tempStatesToProcess.add("dummy")

        var deltaToSearch1: String
        var deltaToSearch2: String

        var deltaResult1 = ArrayList<String>()
        var deltaResult2 = ArrayList<String>()

        var newState: String
        var tempW: String
        var newW: String
        var deltaResultPilePart: String
        var newPile: String

        while(tempStatesToProcess.size > 0){

            tempStatesToProcess.clear()

            for(i in 0..(statesToProcess.size-1)){

                var currentStateParts = statesToProcess.get(i).split('@')

                if(!currentStateParts.get(1).equals("E")){
                    deltaToSearch1 = "delta("+currentStateParts.get(0)+","+currentStateParts.get(1).get(0)+","+currentStateParts.get(2).get(0)+")"
                    deltaToSearch2 = "delta("+currentStateParts.get(0)+",E,"+currentStateParts.get(2).get(0)+")"
                } else {
                    deltaToSearch1 = "dummy"
                    deltaToSearch2 = "delta("+currentStateParts.get(0)+",E,"+currentStateParts.get(2).get(0)+")"
                }

                deltaResult1.clear()
                deltaResult2.clear()

                for(d in 0..(m.globalDeltas.size-1)){
                    if(m.globalDeltas.get(d).contains(deltaToSearch1)){
                        deltaResult1.add(m.globalDeltas.get(d).split('=').get(1))
                    }
                    if(m.globalDeltas.get(d).contains(deltaToSearch2)){
                        deltaResult2.add(m.globalDeltas.get(d).split('=').get(1))
                    }
                }

                if(deltaResult1.size==0 && deltaResult2.size==0){
                    leafNodes.add(statesToProcess.get(i))
                } else {

                    for(p in 0..(deltaResult1.size-1)){
                        newState = deltaResult1.get(p).split('(').get(1).split(',').get(0)

                        tempW = currentStateParts.get(1)
                        newW = tempW.substring(1,tempW.length)
                        if(newW.length==0){
                            newW="E"
                        }

                        //Always POP
                        newPile = currentStateParts.get(2)
                        newPile = newPile.substring(1,newPile.length)

                        deltaResultPilePart = deltaResult1.get(p).split(',').get(1).split(')').get(0)
                        if(!deltaResultPilePart.equals("E")){
                            newPile = deltaResultPilePart + newPile
                        }

                        tempStatesToProcess.add(newState+"@"+newW+"@"+newPile)
                    }


                    for(p in 0..(deltaResult2.size-1)){
                        newState = deltaResult2.get(p).split('(').get(1).split(',').get(0)

                        newW = currentStateParts.get(1)

                        //Always POP
                        newPile = currentStateParts.get(2)
                        newPile = newPile.substring(1,newPile.length)

                        deltaResultPilePart = deltaResult2.get(p).split(',').get(1).split(')').get(0)
                        if(!deltaResultPilePart.equals("E")){
                            newPile = deltaResultPilePart + newPile
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

    fun processTuringMachine(wToEvaluate: String, m: GlobalAutomata): ArrayList<String>{
        var result = ArrayList<String>()

        var newTape = ""
        var theTape = m.globalBlankCursor+wToEvaluate+m.globalBlankCursor
        var iteratorPosition = 1
        var currentState = m.globalInitialState

        var continueInWhile = true
        var deltaFound: String
        var deltaToSearch: String

        while(continueInWhile){
            deltaFound = ""
            if(iteratorPosition>theTape.length-1){
                iteratorPosition = theTape.length-1
            } else if(iteratorPosition<0){
                iteratorPosition = 0
            }
            deltaToSearch = "delta("+currentState + "," + theTape.get(iteratorPosition) + ")"
            for(d in 0..(m.globalDeltas.size-1)){
                if(m.globalDeltas.get(d).contains(deltaToSearch)){
                    deltaFound = m.globalDeltas.get(d).split('=').get(1).split('(').get(1).split(')').get(0)
                    break
                }
            }

            if(deltaFound != ""){
                var deltaParts = deltaFound.split(',')
                currentState = deltaParts.get(0)
                newTape = updateTape(theTape,deltaParts.get(1),iteratorPosition)
                theTape = newTape
                if(deltaParts.get(2).equals("R")){
                    iteratorPosition = iteratorPosition + 1
                } else if(deltaParts.get(2).equals("L")){
                    iteratorPosition = iteratorPosition - 1
                }
            } else {
                continueInWhile = false
            }
        }

        var isFinalStateAcceptance = "0"
        for(i in 0..(m.globalAcceptanceStates.size-1)){
            if(m.globalAcceptanceStates.get(i).equals(currentState)){
                isFinalStateAcceptance = "1"
                break
            }
        }

        result.add(theTape)
        result.add(isFinalStateAcceptance)
        return result
    }

    fun updateTape(theString: String, newCharacter: String, position: Int): String{
        var result = ""
        if(position.equals(0)){
            result = newCharacter + theString.substring(1,theString.length)
        } else if(position.equals(theString.length)){
            result = theString.substring(0,theString.length-1) + newCharacter
        } else {
            result = theString.substring(0,position) + newCharacter + theString.substring(position+1,theString.length)
        }
        return result
    }

}