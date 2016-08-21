/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package MainWindowUtility

import TheGlobalAutomata.*
import TabRenderer.*
import DeltaRenderer.*

import java.awt.*
import java.util.*
import javax.swing.*

class MainUtility{

    fun getAllComponents(c: Container): ArrayList<Component> {
        val comps = c.components
        val compList = ArrayList<Component>()
        for (comp in comps) {
            compList.add(comp)
            if (comp is Container)
                compList.addAll(getAllComponents(comp))
        }
        return compList
    }

    fun <T : Component> getComponentByName(c: Container, name: String): T? {
        val allComponents = getAllComponents(c)
        for(i in 0..(allComponents.size-1)){
            if(allComponents.get(i) != null){
                if(allComponents.get(i).name != null){
                    if (allComponents.get(i).name.equals(name)) {
                        return allComponents.get(i) as T
                    }
                }
            }
        }
        return null
    }

    fun renderMainFunction(m: GlobalAutomata){
        val frame = JFrame("Proyecto Teoria De Computacion")
        frame.setSize(1300, 900)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val generalContainer = Container()
        generalContainer.layout = null
        generalContainer.name = "generalContainer"
        generalContainer.setBounds(0,0,1300,900)

        val tabContainerPanel = JPanel()
        TabPanelRenderer().renderTabPane(frame,generalContainer,tabContainerPanel,m)
        generalContainer.add(tabContainerPanel)
        generalContainer.setComponentZOrder(tabContainerPanel,0)

        val deltaPanel = JPanel()
        DeltaPanelRenderer().initRenderDeltaPanel(generalContainer, deltaPanel)
        DeltaLogic.DeltaPanelLogic().setDeltaPanelVisibility(generalContainer,false)

        frame.getContentPane().add(generalContainer)
        frame.isVisible = true
    }

    fun renderAutomataWithXsAndYs(frame: JFrame, c: Container, tabPanel: JPanel, currentM: GlobalAutomata, newM: GlobalAutomata){

        TabLogic.TabPanelLogic().clearAutomata(c,currentM)

        currentM.globalAutomataType = newM.globalAutomataType
        val typeComboBox = getComponentByName<JComboBox<String>>(c, "t1TypesComboBox")
        if(typeComboBox != null){
            typeComboBox.setSelectedItem(newM.globalAutomataType)
        }

        val automataNameTextBox = getComponentByName<JTextField>(c, "t1NameTextField")
        if(automataNameTextBox != null){
            automataNameTextBox.text = currentM.globalChooser1.selectedFile.name.split('.').get(0)
        }

        var isFirstStateAcceptance = false
        for(i in 0..(newM.globalAcceptanceStates.size-1)){
            if(newM.globalAcceptanceStates.get(i).equals(newM.globalInitialState)){
                isFirstStateAcceptance = true
            }
        }
        TabLogic.TabPanelLogic().addNewStateCore(frame,c,tabPanel,currentM,newM.globalInitialState,isFirstStateAcceptance)

        for(i in 0..(newM.globalStates.size-1)){
            if(!(newM.globalStates.get(i).equals(newM.globalInitialState))){
                var isStateAcceptance = false
                for(j in 0..(newM.globalAcceptanceStates.size-1)){
                    if(newM.globalAcceptanceStates.get(j).equals(newM.globalStates.get(i))){
                        isStateAcceptance = true
                    }
                }
                TabLogic.TabPanelLogic().addNewStateCore(frame,c,tabPanel,currentM,newM.globalStates.get(i),isStateAcceptance)
            }
        }

        currentM.globalAlphabet = newM.globalAlphabet
        currentM.globalGammaAlphabet = newM.globalGammaAlphabet

        val alphabetTextBox = getComponentByName<JTextField>(c, "t3AlphabetTextField")
        if(alphabetTextBox != null){
            alphabetTextBox.text = newM.globalAlphabet
        }

        val gammaAlphabetNameTextBox = getComponentByName<JTextField>(c, "t4AlphabetTextField")
        if(gammaAlphabetNameTextBox != null){
            gammaAlphabetNameTextBox.text = newM.globalGammaAlphabet
        }

        for(p in 0..(newM.globalDeltas.size-1)){
            val currentDelta = newM.globalDeltas.get(p).split('=')

            val initialState = currentDelta.get(0).split('(').get(1).split(',').get(0)
            var finalState = ""
            when (newM.globalAutomataType) {
                "DFA" -> finalState = currentDelta.get(1)
                "NFA" -> finalState = currentDelta.get(1)
                "NFA-E" -> finalState = currentDelta.get(1)
                "PDA" -> finalState = currentDelta.get(1).split('(').get(1).split(',').get(0)
                "Maquina Turing" -> finalState = currentDelta.get(1).split('(').get(1).split(',').get(0)
            }

            var deltaData = ArrayList<String>()
            when (newM.globalAutomataType) {
                "DFA" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataDFA(newM,initialState,finalState)
                "NFA" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataNFA(newM,initialState,finalState)
                "NFA-E" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataNFAE(newM,initialState,finalState)
                "PDA" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataPDA(newM,initialState,finalState)
                "Maquina Turing" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataTuring(newM,initialState,finalState)
            }

            for(k in 0..(deltaData.size-1)){
                TabLogic.TabPanelLogic().addTransitionCore(frame,c,tabPanel,currentM,deltaData.get(k),initialState,finalState)
            }
        }

        for(i in 0..(newM.globalXsAndYs.size-1)){
            var currentData = newM.globalXsAndYs.get(i).split(',')
            GraphUtility.AutomataGraphUtility().setXandY(currentM,currentData.get(0),currentData.get(1),currentData.get(2))
        }
    }

    fun renderAutomataWithoutXsAndYs(frame: JFrame, c: Container, tabPanel: JPanel, currentM: GlobalAutomata, newM: GlobalAutomata, newAutomataName: String){
        TabLogic.TabPanelLogic().clearAutomata(c,currentM)

        currentM.globalAutomataType = newM.globalAutomataType
        val typeComboBox = getComponentByName<JComboBox<String>>(c, "t1TypesComboBox")
        if(typeComboBox != null){
            typeComboBox.setSelectedItem(newM.globalAutomataType)
        }

        val automataNameTextBox = getComponentByName<JTextField>(c, "t1NameTextField")
        if(automataNameTextBox != null){
            automataNameTextBox.text = newAutomataName
        }

        var isFirstStateAcceptance = false
        for(i in 0..(newM.globalAcceptanceStates.size-1)){
            if(newM.globalAcceptanceStates.get(i).equals(newM.globalInitialState)){
                isFirstStateAcceptance = true
            }
        }
        TabLogic.TabPanelLogic().addNewStateCore(frame,c,tabPanel,currentM,newM.globalInitialState,isFirstStateAcceptance)

        for(i in 0..(newM.globalStates.size-1)){
            if(!(newM.globalStates.get(i).equals(newM.globalInitialState))){
                var isStateAcceptance = false
                for(j in 0..(newM.globalAcceptanceStates.size-1)){
                    if(newM.globalAcceptanceStates.get(j).equals(newM.globalStates.get(i))){
                        isStateAcceptance = true
                    }
                }
                TabLogic.TabPanelLogic().addNewStateCore(frame,c,tabPanel,currentM,newM.globalStates.get(i),isStateAcceptance)
            }
        }

        GraphUtility.AutomataGraphUtility().createXsAndYsForStatesOnly(newM)
        for(i in 0..(newM.globalXsAndYs.size-1)){
            var currentData = newM.globalXsAndYs.get(i).split(',')
            GraphUtility.AutomataGraphUtility().setXandY(currentM,currentData.get(0),currentData.get(1),currentData.get(2))
        }

        currentM.globalAlphabet = newM.globalAlphabet
        currentM.globalGammaAlphabet = newM.globalGammaAlphabet

        val alphabetTextBox = getComponentByName<JTextField>(c, "t3AlphabetTextField")
        if(alphabetTextBox != null){
            alphabetTextBox.text = newM.globalAlphabet
        }

        val gammaAlphabetNameTextBox = getComponentByName<JTextField>(c, "t4AlphabetTextField")
        if(gammaAlphabetNameTextBox != null){
            gammaAlphabetNameTextBox.text = newM.globalGammaAlphabet
        }

        for(p in 0..(newM.globalDeltas.size-1)){
            val currentDelta = newM.globalDeltas.get(p).split('=')

            val initialState = currentDelta.get(0).split('(').get(1).split(',').get(0)
            var finalState = ""
            when (newM.globalAutomataType) {
                "DFA" -> finalState = currentDelta.get(1)
                "NFA" -> finalState = currentDelta.get(1)
                "NFA-E" -> finalState = currentDelta.get(1)
                "PDA" -> finalState = currentDelta.get(1).split('(').get(1).split(',').get(0)
                "Maquina Turing" -> finalState = currentDelta.get(1).split('(').get(1).split(',').get(0)
            }

            var deltaData = ArrayList<String>()
            when (newM.globalAutomataType) {
                "DFA" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataDFA(newM,initialState,finalState)
                "NFA" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataNFA(newM,initialState,finalState)
                "NFA-E" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataNFAE(newM,initialState,finalState)
                "PDA" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataPDA(newM,initialState,finalState)
                "Maquina Turing" -> deltaData = DeltaRenderer.DeltaPanelRenderer().renderDeltaDataTuring(newM,initialState,finalState)
            }

            for(k in 0..(deltaData.size-1)){
                TabLogic.TabPanelLogic().addTransitionCore(frame,c,tabPanel,currentM,deltaData.get(k),initialState,finalState)
            }
        }
    }

}