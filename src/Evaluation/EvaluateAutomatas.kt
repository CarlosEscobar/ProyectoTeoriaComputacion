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
        return result
    }

}