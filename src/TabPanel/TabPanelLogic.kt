/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package TabLogic

import TheGlobalAutomata.*
import MainWindowUtility.*
import DeltaLogic.*
import GraphUtility.*
import FilesUtility.*
import OperationsAndActions.*
import EvaluateUtility.*

import java.awt.*
import java.util.*
import javax.swing.*

class TabPanelLogic{

    //Tab1: Automata

    fun saveAutomata(c: Container, m: GlobalAutomata){
        val automataName = MainUtility().getComponentByName<JTextField>(c, "t1NameTextField")
        if(automataName != null) {
            if(automataName.text != "") {
                AutomataFilesUtility().writeAutomataFile(c, m, automataName.text)
            }
        }
    }

    fun setERLabelAndTextFieldVisibility(c: Container, b: Boolean){
        val ERlabel = MainUtility().getComponentByName<JLabel>(c,"t1ERLabel")
        if( ERlabel != null){
            ERlabel.isVisible = b
        }

        val ERtextField = MainUtility().getComponentByName<JTextField>(c,"t1ERTextField")
        if( ERtextField != null){
            ERtextField.isVisible = b
        }
    }

    fun onChangeEventAutomataType(c: Container, m: GlobalAutomata){
        clearAutomata(c,m)

        val typesComboBox = MainUtility().getComponentByName<JComboBox<String>>(c,"t1TypesComboBox")
        if( typesComboBox != null){
            m.globalAutomataType = typesComboBox.selectedItem.toString()
        }

        if(m.globalAutomataType.equals("ER")){
            setERLabelAndTextFieldVisibility(c,true)
        } else {
            setERLabelAndTextFieldVisibility(c,false)
        }
    }

    fun clearAutomata(c: Container, m: GlobalAutomata){
        clearNewState(c)
        clearAlphabet(c,m)
        clearGammaAlphabet(c,m)
        clearAddTransition(c)
        clearEvaluate(c)
        clearActions(c)

        val automataName = MainUtility().getComponentByName<JTextField>(c, "t1NameTextField")
        if(automataName != null) {
            automataName.text = ""
        }

        val regularExpresion = MainUtility().getComponentByName<JTextField>(c, "t1ERTextField")
        if(regularExpresion != null) {
            regularExpresion.text = ""
        }

        DeltaPanelLogic().setDeltaPanelVisibility(c,false)

        m.globalStates = ArrayList<String>()
        m.globalInitialState = ""
        m.globalAcceptanceStates = ArrayList<String>()
        m.globalAlphabet = ""
        m.globalDeltas = ArrayList<String>()

        m.globalGraph.removeCells(m.globalGraph.getChildCells(m.globalGraph.defaultParent,true,true))
    }

    //Tab2: Add State

    fun addNewState(frame: JFrame, c: Container, tabPanel: JPanel, m: GlobalAutomata){
        val newStateName = MainUtility().getComponentByName<JTextField>(c, "t2NameTextField")
        val newStateAcceptance = MainUtility().getComponentByName<JCheckBox>(c, "t2AcceptanceCheckBox")

        if((newStateName != null) && (newStateAcceptance != null)){
            if(!newStateName.text.equals("")){
                addNewStateCore(frame,c,tabPanel,m,newStateName.text,newStateAcceptance.isSelected)
            }
        }
    }

    fun addNewStateCore(frame: JFrame, c: Container, tabPanel: JPanel, m: GlobalAutomata, stateName: String, isAcceptance: Boolean){
        var newStateAlreadyExists = false

        for(i in 0..(m.globalStates.size-1)){
            if(m.globalStates.get(i).equals(stateName)){
                newStateAlreadyExists = true
            }
        }

        if(!newStateAlreadyExists){
            if(m.globalStates.size == 0){
                m.globalInitialState = stateName
            }
            m.globalStates.add(stateName)
            if(isAcceptance){
                m.globalAcceptanceStates.add(stateName)
            }
            AutomataGraphUtility().drawCircle(frame,c,tabPanel,stateName,isAcceptance, m)
        }
        clearNewState(c)
    }

    fun clearNewState(c: Container){
        val newStateName = MainUtility().getComponentByName<JTextField>(c, "t2NameTextField")
        if(newStateName != null) {
            newStateName.text = ""
        }

        val newStateAcceptance = MainUtility().getComponentByName<JCheckBox>(c, "t2AcceptanceCheckBox")
        if(newStateAcceptance != null) {
            newStateAcceptance.setSelected(false)
        }
    }

    //Tab3: Alfabeto

    fun addCharacterToAlphabet(c: Container, m: GlobalAutomata){
        val newCharacter = MainUtility().getComponentByName<JTextField>(c, "t3NewCharacterTextField")
        val theAlphabet = MainUtility().getComponentByName<JTextField>(c, "t3AlphabetTextField")

        if((newCharacter != null) && (theAlphabet != null)){
            if(!newCharacter.text.equals("")){

                if(theAlphabet.text.equals("")){
                    theAlphabet.text = newCharacter.text
                }else {
                    var newCharacterAlreadyExists = false

                    if(theAlphabet.text.contains(',')){
                        val currentCharacters = theAlphabet.text.split(',')
                        for(i in 0..(currentCharacters.size-1)){
                            if(currentCharacters.get(i).equals(newCharacter.text)){
                                newCharacterAlreadyExists = true
                            }
                        }
                    } else {
                        if(theAlphabet.text.equals(newCharacter.text)){
                            newCharacterAlreadyExists = true
                        }
                    }

                    if(!newCharacterAlreadyExists){
                        theAlphabet.text = theAlphabet.text + "," + newCharacter.text
                    }
                }
            }
            newCharacter.text = ""
            m.globalAlphabet = theAlphabet.text
        }
    }

    fun clearAlphabet(c: Container, m: GlobalAutomata){
        val newCharacter = MainUtility().getComponentByName<JTextField>(c, "t3NewCharacterTextField")
        if(newCharacter != null) {
            newCharacter.text = ""
        }

        val theAlphabet = MainUtility().getComponentByName<JTextField>(c, "t3AlphabetTextField")
        if(theAlphabet != null) {
            theAlphabet.text = ""
        }

        m.globalAlphabet = ""
    }

    //Tab4: Alfabeto Gamma

    fun addCharacterToGammaAlphabet(c: Container, m: GlobalAutomata){
        val newCharacter = MainUtility().getComponentByName<JTextField>(c, "t4NewCharacterTextField")
        val theGammaAlphabet = MainUtility().getComponentByName<JTextField>(c, "t4AlphabetTextField")
        val theAlphabet = MainUtility().getComponentByName<JTextField>(c, "t3AlphabetTextField")

        if((newCharacter != null) && (theGammaAlphabet != null) && (theAlphabet != null)){
            if(!newCharacter.text.equals("")){

                var newCharactersExistsInNormalAlphabet = false
                if(theAlphabet.text.contains(',')){
                    val currentCharacters = theAlphabet.text.split(',')
                    for(i in 0..(currentCharacters.size-1)){
                        if(currentCharacters.get(i).equals(newCharacter.text)){
                            newCharactersExistsInNormalAlphabet = true
                        }
                    }
                } else {
                    if(theAlphabet.text.equals(newCharacter.text)){
                        newCharactersExistsInNormalAlphabet = true
                    }
                }

                if(newCharactersExistsInNormalAlphabet) {

                    if (theGammaAlphabet.text.equals("")) {
                        theGammaAlphabet.text = newCharacter.text
                    } else {
                        var newCharacterAlreadyExists = false
                        if (theGammaAlphabet.text.contains(',')) {
                            val currentCharacters = theGammaAlphabet.text.split(',')
                            for (i in 0..(currentCharacters.size - 1)) {
                                if (currentCharacters.get(i).equals(newCharacter.text)) {
                                    newCharacterAlreadyExists = true
                                }
                            }
                        } else {
                            if (theGammaAlphabet.text.equals(newCharacter.text)) {
                                newCharacterAlreadyExists = true
                            }
                        }

                        if (!newCharacterAlreadyExists)  {
                            theGammaAlphabet.text = theGammaAlphabet.text + "," + newCharacter.text
                        }
                    }

                }
            }
            newCharacter.text = ""
            m.globalGammaAlphabet = theGammaAlphabet.text
        }
    }

    fun clearGammaAlphabet(c: Container, m: GlobalAutomata){
        val newCharacter = MainUtility().getComponentByName<JTextField>(c, "t4NewCharacterTextField")
        if(newCharacter != null) {
            newCharacter.text = ""
        }

        val theAlphabet = MainUtility().getComponentByName<JTextField>(c, "t4AlphabetTextField")
        if(theAlphabet != null) {
            theAlphabet.text = ""
        }

        m.globalGammaAlphabet = ""
    }

    //Tab5: Aristas

    fun addTransition(frame: JFrame, c: Container, tabPanel: JPanel, m: GlobalAutomata){
        val firstState = MainUtility().getComponentByName<JTextField>(c, "t5FirstStateTextField")
        val lastState = MainUtility().getComponentByName<JTextField>(c, "t5LastStateTextField")
        val transitionText = MainUtility().getComponentByName<JTextField>(c, "t5CharacterTextField")

        if((firstState != null) && (lastState != null) && (transitionText != null)){
            if((!m.globalAlphabet.equals("")) && (!transitionText.text.equals(""))){
                addTransitionCore(frame,c,tabPanel,m,transitionText.text,firstState.text,lastState.text)
            }
        }
    }

    fun addTransitionCore(frame: JFrame, c: Container, tabPanel: JPanel, m: GlobalAutomata, transitionText: String, firstState: String, lastState: String){
        var isTransitionCharacterOnAlphabet = false
        val currentAutomataType = m.globalAutomataType
        var transitionCharacter = ""

        when (currentAutomataType) {
            "DFA" -> transitionCharacter = transitionText
            "NFA" -> transitionCharacter = transitionText
            "NFA-E" -> transitionCharacter = transitionText
            "PDA" -> transitionCharacter = transitionText.split(',').get(0)
            "Maquina Turing" -> transitionCharacter = getTransitionCharacterFromTuring(transitionText)
        }

        val theAlphabet = m.globalAlphabet
        if(theAlphabet.contains(',')){
            val currentCharacters = theAlphabet.split(',')
            for(i in 0..(currentCharacters.size-1)){
                if(currentCharacters.get(i).equals(transitionCharacter)){
                    isTransitionCharacterOnAlphabet = true
                }
            }
        } else {
            if(theAlphabet.equals(transitionCharacter)){
                isTransitionCharacterOnAlphabet = true
            }
        }

        if(isTransitionCharacterOnAlphabet){

            var deltaToAdd = ""
            when (currentAutomataType) {
                "DFA" -> deltaToAdd = composeDeltaNFA_NFAE(firstState,transitionText, lastState)
                "NFA" -> deltaToAdd = composeDeltaNFA_NFAE(firstState,transitionText, lastState)
                "NFA-E" -> deltaToAdd = composeDeltaNFA_NFAE(firstState,transitionText, lastState)
                "PDA" -> deltaToAdd = composeDeltaPDA(firstState,transitionText, lastState)
                "Maquina Turing" -> deltaToAdd = composeDeltaTuring(firstState,transitionText, lastState)
            }

            var doesNewDeltaAlreadyExist = false
            for(r in 0..(m.globalDeltas.size-1)){
                if(m.globalDeltas.get(r).equals(deltaToAdd)){
                    doesNewDeltaAlreadyExist = true
                }
            }

            if(!doesNewDeltaAlreadyExist){
                AutomataGraphUtility().drawTransition(frame,c,tabPanel,firstState,lastState, m)

                when (currentAutomataType) {
                    "DFA" -> addTransitionDFA(m,firstState,transitionText, lastState)
                    "NFA" -> addTransitionNFA(m,firstState,transitionText, lastState)
                    "NFA-E" -> addTransitionNFAE(m,firstState,transitionText, lastState)
                    "PDA" -> addTransitionPDA(m,firstState,transitionText, lastState)
                    "Maquina Turing" -> addTransitionTuring(m,firstState,transitionText, lastState)
                }
            }
            clearAddTransition(c)
        }
    }

    fun getTransitionCharacterFromTuring(transitionText: String): String{
        val part1 = transitionText.split('/').get(1)
        var result = ""
        if(part1.contains('L')){
            result = part1.split('L').get(0)
        } else if(part1.contains('R')){
            result = part1.split('R').get(0)
        }
        return result
    }

    fun addTransitionDFA(m: GlobalAutomata, firstState: String, transitionText: String, lastState: String){
        val newDelta = composeDeltaDFA(m,firstState,transitionText,lastState)
        m.globalDeltas.add(newDelta)
    }

    fun composeDeltaDFA(m: GlobalAutomata, firstState: String, transitionText: String, lastState: String): String{
        var result = ""
        if(m.globalDeltas.size==0){
            result = "delta("+firstState+","+transitionText+")="+lastState
        } else {

            var transitionExists = false
            for(i in 0..(m.globalDeltas.size-1)){
                val currentDelta = m.globalDeltas.get(i)
                val deltaData = currentDelta.split('(').get(1).split(')').get(0).split(',')
                if((deltaData.get(0).equals(firstState)) && (deltaData.get(1).equals(transitionText))){
                    transitionExists = true
                }
            }

            if(!transitionExists){
                result = "delta("+firstState+","+transitionText+")="+lastState
            }
        }
        return result
    }

    fun addTransitionNFA(m: GlobalAutomata, firstState: String, transitionText: String, lastState: String){
        val newDelta = composeDeltaNFA_NFAE(firstState,transitionText,lastState)
        m.globalDeltas.add(newDelta)
    }

    fun addTransitionNFAE(m: GlobalAutomata, firstState: String, transitionText: String, lastState: String){
        val newDelta = composeDeltaNFA_NFAE(firstState,transitionText,lastState)
        m.globalDeltas.add(newDelta)
    }

    fun composeDeltaNFA_NFAE(firstState: String, transitionText: String, lastState: String): String{
        return "delta("+firstState+","+transitionText+")="+lastState
    }

    fun addTransitionPDA(m: GlobalAutomata, firstState: String, transitionText: String, lastState: String){
        val newDelta = composeDeltaPDA(firstState,transitionText,lastState)
        m.globalDeltas.add(newDelta)
    }

    fun composeDeltaPDA(firstState: String, transitionText: String, lastState: String): String{
        val deltaPart = transitionText.split('/')
        return "delta("+firstState+","+deltaPart.get(0)+")=("+lastState+","+deltaPart.get(1)+")"
    }

    fun addTransitionTuring(m: GlobalAutomata, firstState: String, transitionText: String, lastState: String){
        val newDelta = composeDeltaTuring(firstState,transitionText,lastState)
        m.globalDeltas.add(newDelta)
    }

    fun composeDeltaTuring(firstState: String, transitionText: String, lastState: String): String{
        val deltaPart = transitionText.split('/')

        val part1 = deltaPart.get(1)
        var transitionCharacter = ""
        var direction = ""
        if(part1.contains('L')){
            transitionCharacter = part1.split('L').get(0)
            direction = "L"
        } else if(part1.contains('R')){
            transitionCharacter = part1.split('R').get(0)
            direction = "R"
        }

        return "delta("+firstState+","+deltaPart.get(0)+")=("+lastState+","+transitionCharacter+","+direction+")"
    }

    fun clearAddTransition(c: Container){
        val firstState = MainUtility().getComponentByName<JTextField>(c, "t5FirstStateTextField")
        if(firstState != null) {
            firstState.text = ""
        }

        val lastState = MainUtility().getComponentByName<JTextField>(c, "t5LastStateTextField")
        if(lastState != null) {
            lastState.text = ""
        }

        val transitionCharacter = MainUtility().getComponentByName<JTextField>(c, "t5CharacterTextField")
        if(transitionCharacter != null) {
            transitionCharacter.text = ""
        }
    }

    //Tab6: Acciones

    fun chooseFile1(c: Container, chooser: JFileChooser){
        val returnVal = chooser.showOpenDialog(null)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file1TextBox = MainUtility().getComponentByName<JTextField>(c, "t6FirstAutomataTextField")
            if(file1TextBox != null) {
                file1TextBox.text = chooser.selectedFile.name
            }
        }
    }

    fun chooseFile2(c: Container, chooser: JFileChooser){
        val returnVal = chooser.showOpenDialog(null)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file2TextBox = MainUtility().getComponentByName<JTextField>(c, "t6SecondAutomataTextField")
            if(file2TextBox != null) {
                file2TextBox.text = chooser.selectedFile.name
            }
        }
    }

    fun executeAction(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        AutomataOperationsAndActions().executeAction(frame,c,panel,m)
    }

    fun clearActions(c: Container){
        val fileTextBox1 = MainUtility().getComponentByName<JTextField>(c, "t6FirstAutomataTextField")
        if(fileTextBox1 != null) {
            fileTextBox1.text = ""
        }

        val fileTextBox2 = MainUtility().getComponentByName<JTextField>(c, "t6SecondAutomataTextField")
        if(fileTextBox2 != null) {
            fileTextBox2.text = ""
        }
    }

    //Tab7: Evaluar

    fun evaluate(c: Container, m: GlobalAutomata){
        EvaluateAutomatas().evaluate(c,m)
    }

    fun clearEvaluate(c: Container){
        val stringToEvaluate = MainUtility().getComponentByName<JTextField>(c, "t7StringTextField")
        if(stringToEvaluate != null) {
            stringToEvaluate.text = ""
        }

        val resultLabel = MainUtility().getComponentByName<JLabel>(c, "t7ResultLabel")
        if(resultLabel != null) {
            resultLabel.text = ""
        }

        val tapeLabel = MainUtility().getComponentByName<JLabel>(c,"t7TapeLabel")
        if(tapeLabel != null){
            tapeLabel.text = ""
        }
    }

    //Tab8: Ayuda

}