/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package DeltaRenderer

import TheGlobalAutomata.*
import MainWindowUtility.*
import DeltaLogic.*

import java.awt.Container
import java.awt.event.*
import java.util.*
import javax.swing.*

class DeltaPanelRenderer{

    fun initRenderDeltaPanel(c: Container, panel: JPanel){
        panel.layout = null
        panel.name = "deltaPanel"
        panel.setBounds(90,350,250,450)
        panel.background = java.awt.Color.decode("#B8DBFF")

        val titlePanel = JPanel()
        titlePanel.layout = null
        titlePanel.name = "titleDeltaPanel"
        titlePanel.setBounds(90,300,250,50)
        titlePanel.background = java.awt.Color.decode("#1C8AFF")

        val titleLabel = JLabel()
        titleLabel.name = "titleLabelDeltaPanel"
        titleLabel.text = ""
        titlePanel.add(titleLabel)
        titleLabel.setBounds(20,10,200,30)

        val closeButton = JButton("X")
        closeButton.name = "closeDeltaPanelButton"
        titlePanel.add(closeButton)
        closeButton.setBounds(195,10,45,30)
        closeButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                DeltaPanelLogic().setDeltaPanelVisibility(c,false)
            }
        })

        c.add(titlePanel)
        titlePanel.isVisible = false
        c.setComponentZOrder(titlePanel,1)

        c.add(panel)
        panel.isVisible = false
        c.setComponentZOrder(panel,2)
    }

    fun renderDeltaData(c: Container, initialState: String, finalState: String, m: GlobalAutomata){

        val deltaPanelTitle = "Arista: " + initialState + "  - >  " + finalState

        val titleLabelDeltaPanel = MainUtility().getComponentByName<JLabel>(c, "titleLabelDeltaPanel")
        if(titleLabelDeltaPanel != null) {
            titleLabelDeltaPanel.text = deltaPanelTitle
        }

        var deltas = ArrayList<String>()
        val currentAutomataType = m.globalAutomataType
        when (currentAutomataType) {
            "DFA" -> deltas = renderDeltaDataDFA(m,initialState,finalState)
            "NFA" -> deltas = renderDeltaDataNFA(m,initialState,finalState)
            "NFA-E" -> deltas = renderDeltaDataNFAE(m,initialState, finalState)
            "PDA" -> deltas = renderDeltaDataPDA(m,initialState, finalState)
            "Maquina Turing" -> deltas = renderDeltaDataTuring(m,initialState,finalState)
        }

        val deltaPanel = MainUtility().getComponentByName<JPanel>(c, "deltaPanel")
        if(deltaPanel != null){

            val oldTempPanel = MainUtility().getComponentByName<JPanel>(c, "tempPanel")
            if(oldTempPanel != null){
                deltaPanel.remove(oldTempPanel)
            }

            val tempPanel = JPanel()
            tempPanel.layout = null
            tempPanel.name = "tempPanel"
            tempPanel.background = java.awt.Color.decode("#B8DBFF")

            var offsetY = 20
            for(i in 0..(deltas.size-1)){
                val newLabel = JLabel()
                newLabel.text = deltas.get(i).toString()
                tempPanel.add(newLabel)
                newLabel.setBounds(100,offsetY,200,30)
                offsetY = offsetY + 40
            }

            deltaPanel.add(tempPanel)
            deltaPanel.setBounds(90,350,250,offsetY)
            tempPanel.setBounds(0,0,210,offsetY)
            DeltaPanelLogic().setDeltaPanelVisibility(c,true)
        }
    }

    fun renderDeltaDataDFA(m: GlobalAutomata, initialState: String, finalState: String):ArrayList<String>{
        var result = ArrayList<String>()

        for(i in 0..(m.globalDeltas.size-1)){
            val deltaData = m.globalDeltas.get(i)
            val delta1 = deltaData.split('=')
            val delta2 = delta1.get(0).split('(').get(1).split(',')
            val firstState =  delta2.get(0)
            val lastState = delta1.get(1)

            if((firstState.equals(initialState)) && (lastState.equals(finalState))){
                val transitionText = delta2.get(1).split(')').get(0)
                result.add(transitionText)
            }
        }

        return result
    }

    fun renderDeltaDataNFA(m: GlobalAutomata, initialState: String, finalState: String):ArrayList<String>{
        var result = ArrayList<String>()

        for(i in 0..(m.globalDeltas.size-1)){
            val deltaData = m.globalDeltas.get(i)
            val delta1 = deltaData.split('=')
            val delta2 = delta1.get(0).split('(').get(1).split(',')
            val firstState =  delta2.get(0)
            val lastState = delta1.get(1)

            if((firstState.equals(initialState)) && (lastState.equals(finalState))){
                val transitionText = delta2.get(1).split(')').get(0)
                result.add(transitionText)
            }
        }

        return result
    }

    fun renderDeltaDataNFAE(m: GlobalAutomata, initialState: String, finalState: String):ArrayList<String>{
        var result = ArrayList<String>()

        for(i in 0..(m.globalDeltas.size-1)){
            val deltaData = m.globalDeltas.get(i)
            val delta1 = deltaData.split('=')
            val delta2 = delta1.get(0).split('(').get(1).split(',')
            val firstState =  delta2.get(0)
            val lastState = delta1.get(1)

            if((firstState.equals(initialState)) && (lastState.equals(finalState))){
                val transitionText = delta2.get(1).split(')').get(0)
                result.add(transitionText)
            }
        }

        return result
    }

    fun renderDeltaDataPDA(m: GlobalAutomata, initialState: String, finalState: String):ArrayList<String>{
        var result = ArrayList<String>()

        for(i in 0..(m.globalDeltas.size-1)){
            val deltaData = m.globalDeltas.get(i)
            val delta1 = deltaData.split('=')
            val delta2 = delta1.get(0).split('(').get(1).split(',')
            val delta3 = delta1.get(1).split(',')
            val firstState =  delta2.get(0)
            val lastState = delta3.get(0).split('(').get(1)

            if((firstState.equals(initialState)) && (lastState.equals(finalState))){
                val transitionText = delta2.get(1) + "," + delta2.get(2).split(')').get(0) + "/" + delta3.get(1).split(')').get(0)
                result.add(transitionText)
            }
        }

        return result
    }

    fun renderDeltaDataTuring(m: GlobalAutomata, initialState: String, finalState: String):ArrayList<String>{
        var result = ArrayList<String>()

        for(i in 0..(m.globalDeltas.size-1)){
            val deltaData = m.globalDeltas.get(i)
            val delta1 = deltaData.split('=')
            val delta2 = delta1.get(0).split('(').get(1).split(',')
            val delta3 = delta1.get(1).split(',')
            val firstState =  delta2.get(0)
            val lastState = delta3.get(0).split('(').get(1)

            if((firstState.equals(initialState)) && (lastState.equals(finalState))){
                val transitionText = delta2.get(1).split(')').get(0) + "/" + delta3.get(1) + delta3.get(2).split(')').get(0)
                result.add(transitionText)
            }
        }

        return result
    }

}