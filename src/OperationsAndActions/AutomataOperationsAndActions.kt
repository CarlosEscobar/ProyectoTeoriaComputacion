/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package OperationsAndActions

import FilesUtility.*
import TheGlobalAutomata.*
import MainWindowUtility.*
import RegExTreeBuilder.TreeDefinition.*

import java.awt.Container
import javax.swing.*
import java.util.*
import kotlin.reflect.KMutableProperty

class AutomataOperationsAndActions{

    fun executeAction(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val actionComboBox = MainUtility().getComponentByName<JComboBox<String>>(c, "t6ActionComboBox")
        if((actionComboBox != null)){
            var todo = 0.0
            when (actionComboBox.selectedItem.toString()) {
                " - Convertir DFA-ER" -> todo = 1.1
                " - Convertir NFA-DFA" -> actionConvert_NFA_DFA(frame,c,panel,m)
                " - Convertir NFA-ER" -> todo = 1.2
                " - Convertir NFAE-NFA" -> actionConvert_NFAE_NFA(frame,c,panel,m)
                " - Convertir NFAE-DFA" -> actionConvert_NFAE_DFA(frame,c,panel,m)
                " - Convertir NFAE-ER" -> todo = 1.3
                " - Convertir ER-NFAE" -> actionConvert_ER_NFAE(frame,c,panel,m)
                " - Convertir ER-NFA" -> actionConvert_ER_NFA(frame,c,panel,m)
                " - Convertir ER-DFA" -> actionConvert_ER_DFA(frame,c,panel,m)
                " - Convertir Gramatica-PDA" -> actionConvert_CFG_PDA(frame,c,panel,m)
                " - Cargar Automata" -> actionLoadAutomata(frame,c,panel,m)
                " - Complemento Automata" -> actionComplement(frame,c,panel,m)
                " - Minimizacion Automata" -> actionMinimize(frame,c,panel,m)
                " - Union Automatas" -> actionUnion(frame,c,panel,m)
                " - Interseccion Automatas" -> actionIntersection(frame,c,panel,m)
                " - Diferencia Automatas" -> actionDifference(frame,c,panel,m)
            }
        }
    }

    fun actionLoadAutomata(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){

        val automataAbsolutePath = m.globalChooser1.selectedFile.absolutePath
        val automataFromFileType = m.globalChooser1.selectedFile.parentFile.name

        if(automataFromFileType.equals("ER")){

            TabLogic.TabPanelLogic().clearAutomata(c,m)

            m.globalAutomataType = "ER"
            val typeComboBox = MainUtility().getComponentByName<JComboBox<String>>(c, "t1TypesComboBox")
            if(typeComboBox != null){
                typeComboBox.setSelectedItem("ER")
            }

            val automataNameTextBox = MainUtility().getComponentByName<JTextField>(c, "t1NameTextField")
            if(automataNameTextBox != null){
                automataNameTextBox.text = m.globalChooser1.selectedFile.name.split('.').get(0)
            }

            val regularExpresionFromFile = AutomataFilesUtility().readFile(automataAbsolutePath)
            val ERtextField = MainUtility().getComponentByName<JTextField>(c, "t1ERTextField")
            if(ERtextField != null) {
                ERtextField.text = regularExpresionFromFile
            }



        } else {
            val automataFromFile = AutomataFilesUtility().readAutomataFile(automataAbsolutePath, automataFromFileType)
            MainUtility().renderAutomataWithXsAndYs(frame, c, panel, m, automataFromFile)
        }
    }

    fun actionConvert_NFA_DFA(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val transformedAutomata = NFA_To_DFA(automataFromFile)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,transformedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_NFA_DFA")
    }

    fun actionConvert_NFAE_NFA(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val transformedAutomata = NFAE_To_NFA(automataFromFile)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,transformedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"NFAE_NFA")
    }

    fun actionConvert_NFAE_DFA(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val transformedAutomataNFA = NFAE_To_NFA(automataFromFile)
        val transformedAutomataDFA = NFA_To_DFA(transformedAutomataNFA)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,transformedAutomataDFA,m.globalChooser1.selectedFile.name.split('.').get(0)+"NFAE_DFA")
    }

    fun actionUnion(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile1 = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val automataFromFile2 = AutomataFilesUtility().readAutomataFile(m.globalChooser2.selectedFile.absolutePath, m.globalChooser2.selectedFile.parentFile.name)
        val unionedAutomata = operateTwoAutomatas(automataFromFile1,automataFromFile2,"union")
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,unionedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_"+m.globalChooser2.selectedFile.name.split('.').get(0)+"_UNION")
    }

    fun actionIntersection(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile1 = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val automataFromFile2 = AutomataFilesUtility().readAutomataFile(m.globalChooser2.selectedFile.absolutePath, m.globalChooser2.selectedFile.parentFile.name)
        val intersectedAutomata = operateTwoAutomatas(automataFromFile1,automataFromFile2,"intersection")
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,intersectedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_"+m.globalChooser2.selectedFile.name.split('.').get(0)+"_INTERSECTION")
    }

    fun actionDifference(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile1 = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val automataFromFile2 = AutomataFilesUtility().readAutomataFile(m.globalChooser2.selectedFile.absolutePath, m.globalChooser2.selectedFile.parentFile.name)
        val diffAutomata = operateTwoAutomatas(automataFromFile1,automataFromFile2,"difference")
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,diffAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_"+m.globalChooser2.selectedFile.name.split('.').get(0)+"_DIFF")
    }

    fun actionComplement(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val complementedAutomata = complementAutomata(automataFromFile)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,complementedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_COMPLEMENT")
    }

    fun actionMinimize(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val automataFromFile = AutomataFilesUtility().readAutomataFile(m.globalChooser1.selectedFile.absolutePath, m.globalChooser1.selectedFile.parentFile.name)
        val minimizedAutomata = minimizeAutomata(automataFromFile)
        //MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,minimizedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_MIN")
    }

    fun actionConvert_ER_NFAE(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val ERfromFile = AutomataFilesUtility().readFile(m.globalChooser1.selectedFile.absolutePath).trim()
        val transformedAutomata = ER_To_NFAE(ERfromFile)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,transformedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_ER_NFAE")
    }

    fun actionConvert_ER_NFA(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val ERfromFile = AutomataFilesUtility().readFile(m.globalChooser1.selectedFile.absolutePath).trim()
        val NFAEautomata = ER_To_NFAE(ERfromFile)
        val NFAautomata = NFAE_To_NFA(NFAEautomata)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,NFAautomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_ER_NFA")
    }

    fun actionConvert_ER_DFA(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val ERfromFile = AutomataFilesUtility().readFile(m.globalChooser1.selectedFile.absolutePath).trim()
        val NFAEautomata = ER_To_NFAE(ERfromFile)
        val NFAautomata = NFAE_To_NFA(NFAEautomata)
        val DFAautomata = NFA_To_DFA(NFAautomata)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,DFAautomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_ER_DFA")
    }

    fun actionConvert_CFG_PDA(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata){
        val grammarAbsolutePath = m.globalChooser1.selectedFile.absolutePath
        var grammarFromFile = AutomataFilesUtility().readGrammarFile(grammarAbsolutePath)
        var transformedGrammarInPDA = CFG_To_PDA(grammarFromFile)
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,transformedGrammarInPDA,m.globalChooser1.selectedFile.name.split('.').get(0)+"CFG_PDA")
    }

    // Logic for actions

    // Usa | para unir estados
    fun NFA_To_DFA(m: GlobalAutomata): GlobalAutomata{
        var result = GlobalAutomata()

        result.globalAlphabet = m.globalAlphabet
        var theAlphabet = m.globalAlphabet.split(',')
        result.globalInitialState = m.globalInitialState
        result.globalStates.add(m.globalInitialState)

        var isInitialStateAcceptance = false
        for(p in 0..(m.globalAcceptanceStates.size-1)){
            if(m.globalAcceptanceStates.get(p).equals(m.globalInitialState)){
                isInitialStateAcceptance = true
                break
            }
        }
        if(isInitialStateAcceptance){
            result.globalAcceptanceStates.add(m.globalInitialState)
        }

        var continueInWhile = true
        var statesToProcess = ArrayList<String>()
        statesToProcess.add(m.globalInitialState)
        var processedStates = ArrayList<String>()
        var stateToAddName = ""

        while(continueInWhile){

            var possibleStatesToProcess = statesToProcess.subtract(processedStates)
            if(possibleStatesToProcess.size==0){
                continueInWhile = false
            } else {

                var currentState = possibleStatesToProcess.first()
                var statesPerState = ArrayList<String>()
                if(currentState.contains('|')){
                    var statesFromCurrentState = currentState.split('|')
                    for(i in 0..(statesFromCurrentState.size-1)){
                        statesPerState.add(statesFromCurrentState.get(i))
                    }
                } else {
                    statesPerState.add(currentState)
                }

                for(a in 0..(theAlphabet.size-1)){
                    stateToAddName = ""
                    for(k in 0..(statesPerState.size-1)){
                        for(d in 0..(m.globalDeltas.size-1)){
                            var deltaData1 = m.globalDeltas.get(d).split('(').get(1).split('=')
                            var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                            if((deltaData2.get(0).equals(statesPerState.get(k))) && (deltaData2.get(1).equals(theAlphabet.get(a)))){
                                if(stateToAddName.contains('|')){
                                    stateToAddName = stateToAddName + deltaData1.get(1) + "|"
                                } else {
                                    stateToAddName = deltaData1.get(1) + "|"
                                }
                            }
                        }
                    }

                    if(stateToAddName != ""){

                        stateToAddName = stateToAddName.substring(0,stateToAddName.length-1)

                        var stateAlreadyWaiting = false
                        for(p in 0..(statesToProcess.size-1)){
                            if(statesToProcess.get(p).equals(stateToAddName)){
                                stateAlreadyWaiting = true
                            }
                        }

                        if(!stateAlreadyWaiting){
                            statesToProcess.add(stateToAddName)
                            result.globalStates.add(stateToAddName)

                            var isNewStateAcceptance = false
                            if(stateToAddName.contains('|')){
                                val tempStates = stateToAddName.split('|')
                                for(t in 0..(tempStates.size-1)){
                                    for(l in 0..(m.globalAcceptanceStates.size-1)){
                                        if(m.globalAcceptanceStates.get(l).equals(tempStates.get(t))){
                                            isNewStateAcceptance = true
                                            break
                                        }
                                    }
                                    if(isNewStateAcceptance){
                                        break
                                    }
                                }
                            } else {
                                for(l in 0..(m.globalAcceptanceStates.size-1)){
                                    if(m.globalAcceptanceStates.get(l).equals(stateToAddName)){
                                        isNewStateAcceptance = true
                                    }
                                }
                            }

                            if(isNewStateAcceptance){
                                result.globalAcceptanceStates.add(stateToAddName)
                            }

                        }

                        val newDelta = "delta(" + currentState + "," + theAlphabet.get(a) + ")=" + stateToAddName
                        result.globalDeltas.add(newDelta)
                    }
                }
                processedStates.add(currentState)
            }
        }
        return result
    }

    // Usa / para unir estados
    fun NFAE_To_NFA(m: GlobalAutomata): GlobalAutomata{
        var result = GlobalAutomata()
        result.globalAutomataType = "NFA"

        var newAlphabet = ""
        var alphabetTemp = m.globalAlphabet.split(',')
        for(k in 0..(alphabetTemp.size-1)){
            if(!alphabetTemp.get(k).equals("E")){
                if(newAlphabet.contains(',')){
                    newAlphabet = newAlphabet + alphabetTemp.get(k) + ","
                } else {
                    newAlphabet = alphabetTemp.get(k) + ","
                }
            }
        }
        result.globalAlphabet = newAlphabet.substring(0,newAlphabet.length-1)

        var theAlphabet = result.globalAlphabet.split(',')
        result.globalInitialState = clausura(m.globalDeltas,m.globalInitialState)
        result.globalStates.add(result.globalInitialState)

        var isInitialStateAcceptance = false

        var initialStateStates = ArrayList<String>()
        if(result.globalInitialState.contains('/')){
            var tInitialStateStates = result.globalInitialState.split('/')
            for(w in 0..(tInitialStateStates.size-1)){
                initialStateStates.add(tInitialStateStates.get(w))
            }
        } else {
            initialStateStates.add(result.globalInitialState)
        }

        for(i in 0..(initialStateStates.size-1)) {
            if(!isInitialStateAcceptance){
                for (p in 0..(m.globalAcceptanceStates.size - 1)) {
                    if (m.globalAcceptanceStates.get(p).contains(initialStateStates.get(i))) {
                        isInitialStateAcceptance = true
                        break
                    }
                }
            }
        }

        if(isInitialStateAcceptance){
            result.globalAcceptanceStates.add(result.globalInitialState)
        }

        var continueInWhile = true
        var statesToProcess = ArrayList<String>()
        statesToProcess.add(result.globalInitialState)
        var processedStates = ArrayList<String>()
        var stateToAddName = ""

        while(continueInWhile){

            var possibleStatesToProcess = statesToProcess.subtract(processedStates)
            if(possibleStatesToProcess.size==0){
                continueInWhile = false
            } else {

                var currentState = possibleStatesToProcess.first()
                var statesPerState = ArrayList<String>()
                if(currentState.contains('/')){
                    var statesFromCurrentState = currentState.split('/')
                    for(i in 0..(statesFromCurrentState.size-1)){
                        statesPerState.add(statesFromCurrentState.get(i))
                    }
                } else {
                    statesPerState.add(currentState)
                }

                for(a in 0..(theAlphabet.size-1)){
                    stateToAddName = ""
                    for(k in 0..(statesPerState.size-1)){
                        for(d in 0..(m.globalDeltas.size-1)){
                            var deltaData1 = m.globalDeltas.get(d).split('(').get(1).split('=')
                            var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                            if((deltaData2.get(0).equals(statesPerState.get(k))) && (deltaData2.get(1).equals(theAlphabet.get(a)))){
                                if(stateToAddName.contains('/')){
                                    stateToAddName = stateToAddName + deltaData1.get(1) + "/"
                                } else {
                                    stateToAddName = deltaData1.get(1) + "/"
                                }
                            }
                        }
                    }

                    if(stateToAddName != ""){

                        stateToAddName = stateToAddName.substring(0,stateToAddName.length-1)
                        stateToAddName = clausura(m.globalDeltas,stateToAddName)

                        var stateAlreadyWaiting = false
                        for(p in 0..(statesToProcess.size-1)){
                            if(statesToProcess.get(p).equals(stateToAddName)){
                                stateAlreadyWaiting = true
                            }
                        }

                        if(!stateAlreadyWaiting){
                            statesToProcess.add(stateToAddName)
                            result.globalStates.add(stateToAddName)

                            var isNewStateAcceptance = false
                            if(stateToAddName.contains('/')){
                                val tempStates = stateToAddName.split('/')
                                for(t in 0..(tempStates.size-1)){
                                    for(l in 0..(m.globalAcceptanceStates.size-1)){
                                        if(m.globalAcceptanceStates.get(l).equals(tempStates.get(t))){
                                            isNewStateAcceptance = true
                                            break
                                        }
                                    }
                                    if(isNewStateAcceptance){
                                        break
                                    }
                                }
                            } else {
                                for(l in 0..(m.globalAcceptanceStates.size-1)){
                                    if(m.globalAcceptanceStates.get(l).equals(stateToAddName)){
                                        isNewStateAcceptance = true
                                    }
                                }
                            }

                            if(isNewStateAcceptance){
                                result.globalAcceptanceStates.add(stateToAddName)
                            }

                        }

                        val newDelta = "delta(" + currentState + "," + theAlphabet.get(a) + ")=" + stateToAddName
                        result.globalDeltas.add(newDelta)
                    }
                }
                processedStates.add(currentState)
            }
        }
        return result
    }

    // Usa / para unir estados
    fun clausura(deltas: ArrayList<String>, state: String): String{
        var result = state
        var currentResultStates = ArrayList<String>()
        var continueInWhile = true
        var found1 = false

        while(continueInWhile){

            currentResultStates = ArrayList<String>()
            if(result.contains('/')){
                var rResult = result.split('/')
                for(m in 0..(rResult.size-1)){
                    currentResultStates.add(rResult.get(m))
                }
            } else {
                currentResultStates.add(result)
            }

            found1 = false
            for(r in 0..(currentResultStates.size-1)){
                for(p in 0..(deltas.size-1)){
                    var deltaData1 = deltas.get(p).split('(').get(1).split('=')
                    var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')

                    if((deltaData2.get(0).equals(currentResultStates.get(r))) && (deltaData2.get(1).equals("E"))){

                        var stateAlreadyAdded = false
                        var resultStates = currentResultStates

                        for(i in 0..(resultStates.size-1)){
                            if(resultStates.get(i).equals(deltaData1.get(1))){
                                stateAlreadyAdded = true
                            }
                        }

                        if(!stateAlreadyAdded){
                            result = result  + "/" + deltaData1.get(1)
                            found1 = true
                        }
                    }
                }
            }

            if(!found1){
                continueInWhile = false
            }
        }
        return result
    }

    // Usa . para unir estados
    fun operateTwoAutomatas(automata1: GlobalAutomata, automata2: GlobalAutomata, operationType: String): GlobalAutomata{
        var result = GlobalAutomata()

        val automata1Characters = automata1.globalAlphabet.split(',')
        val automata2Characters = automata2.globalAlphabet.split(',')
        val automata1UniqueCharacters = automata1Characters.subtract(automata2Characters)
        val newAlphabetCharacters = automata1UniqueCharacters.union(automata2Characters).toList()
        var newAlphabet = ""
        for(k in 0..(newAlphabetCharacters.size-1)){
            if(k==0){
                newAlphabet = newAlphabetCharacters.get(k)
            } else {
                newAlphabet = newAlphabet + "," + newAlphabetCharacters.get(k)
            }
        }
        result.globalAlphabet = newAlphabet

        val initialState = automata1.globalInitialState + "." + automata2.globalInitialState
        result.globalInitialState = initialState
        result.globalStates.add(initialState)
        var statesToProcess = ArrayList<String>()
        statesToProcess.add(initialState)
        var processedStates = ArrayList<String>()

        var continueInWhile = true
        var considerAutomata1ToProcess = true
        var considerAutomata2ToProcess = true

        var automata1State = ""
        var automata2State = ""

        while(continueInWhile){

            var possibleStatesToProcess = statesToProcess.subtract(processedStates)
            if(possibleStatesToProcess.size==0){
                continueInWhile = false
            } else {

                var currentState = possibleStatesToProcess.first()
                considerAutomata1ToProcess = false
                considerAutomata2ToProcess = false

                if(currentState.contains('.')){
                    considerAutomata1ToProcess = true
                    considerAutomata2ToProcess = true

                    val currentStateParts = currentState.split('.')
                    automata1State = currentStateParts.get(0)
                    automata2State = currentStateParts.get(1)
                } else {
                    for(i in 0..(automata1.globalStates.size-1)){
                        if(automata1.globalStates.get(i).equals(currentState)){
                            considerAutomata1ToProcess = true
                            break
                        }
                    }
                    for(i in 0..(automata2.globalStates.size-1)){
                        if(automata2.globalStates.get(i).equals(currentState)){
                            considerAutomata2ToProcess = true
                            break
                        }
                    }

                    if(considerAutomata1ToProcess){
                        automata1State = currentState
                    }
                    if(considerAutomata2ToProcess){
                        automata2State = currentState
                    }
                }

                var newPossibleState = ""
                for(a in 0..(newAlphabetCharacters.size-1)){
                    newPossibleState = ""
                    var foundDeltaInAutomata1 = false
                    if(considerAutomata1ToProcess){
                        for(d in 0..(automata1.globalDeltas.size-1)){
                            var deltaData1 = automata1.globalDeltas.get(d).split('(').get(1).split('=')
                            var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                            if((deltaData2.get(0).equals(automata1State)) && (deltaData2.get(1).equals(newAlphabetCharacters.get(a)))){
                                newPossibleState = deltaData1.get(1)
                                foundDeltaInAutomata1 = true
                                break
                            }
                        }
                    }
                    if(considerAutomata2ToProcess){
                        for(d in 0..(automata2.globalDeltas.size-1)){
                            var deltaData1 = automata2.globalDeltas.get(d).split('(').get(1).split('=')
                            var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                            if((deltaData2.get(0).equals(automata2State)) && (deltaData2.get(1).equals(newAlphabetCharacters.get(a)))){
                                if(considerAutomata1ToProcess && foundDeltaInAutomata1){
                                    newPossibleState = newPossibleState + "." + deltaData1.get(1)
                                } else {
                                    newPossibleState = deltaData1.get(1)
                                }
                                break
                            }
                        }
                    }

                    if(newPossibleState != ""){

                        var newStateAlreadyAdded = false
                        for(p in 0..(result.globalStates.size-1)){
                            if(result.globalStates.get(p).equals(newPossibleState)){
                                newStateAlreadyAdded = true
                                break
                            }
                        }
                        if(!newStateAlreadyAdded) {
                            result.globalStates.add(newPossibleState)
                        }


                        val newDelta = "delta(" + currentState + "," + newAlphabetCharacters.get(a) + ")=" + newPossibleState
                        result.globalDeltas.add(newDelta)
                        statesToProcess.add(newPossibleState)
                    }
                }
                processedStates.add(currentState)
            }
        }

        var isStateInAutomata1Acceptance = false
        var isStateInAutomata2Acceptance = false
        var isThisStateAcceptance = false

        for(r in 0..(result.globalStates.size-1)){

            val stateParts = ArrayList<String>()
            if(result.globalStates.get(r).contains('.')){
                var tStateParts = result.globalStates.get(r).split('.')
                for(t in 0..(tStateParts.size-1)){
                    stateParts.add(tStateParts.get(t))
                }
            } else {
                stateParts.add(result.globalStates.get(r))
            }

            isStateInAutomata1Acceptance = false
            isStateInAutomata2Acceptance = false

            for(s in 0..(stateParts.size-1)){

                for(i in 0..(automata1.globalAcceptanceStates.size-1)){
                    if(automata1.globalAcceptanceStates.get(i).equals(stateParts.get(s))){
                        isStateInAutomata1Acceptance = true
                        break
                    }
                }

                for(j in 0..(automata2.globalAcceptanceStates.size-1)){
                    if(automata2.globalAcceptanceStates.get(j).equals(stateParts.get(s))){
                        isStateInAutomata2Acceptance = true
                        break
                    }
                }
            }


            isThisStateAcceptance = false

            if(operationType.equals("union")){
                if(isStateInAutomata1Acceptance || isStateInAutomata2Acceptance){
                    isThisStateAcceptance = true
                }
            }

            if(operationType.equals("intersection")){
                if(isStateInAutomata1Acceptance && isStateInAutomata2Acceptance){
                    isThisStateAcceptance = true
                }
            }

            if(operationType.equals("difference")){
                if(isStateInAutomata1Acceptance && (!isStateInAutomata2Acceptance)){
                    isThisStateAcceptance = true
                }
            }

            if(isThisStateAcceptance){
                result.globalAcceptanceStates.add(result.globalStates.get(r))
            }
        }

        return result
    }

    fun complementAutomata(m: GlobalAutomata): GlobalAutomata{
        var result = GlobalAutomata()

        result.globalAlphabet = m.globalAlphabet
        var theAlphabet = m.globalAlphabet.split(',')
        result.globalInitialState = m.globalInitialState

        for(i in 0..(m.globalStates.size-1)){
            result.globalStates.add(m.globalStates.get(i))
        }

        for(i in 0..(m.globalDeltas.size-1)){
            result.globalDeltas.add(m.globalDeltas.get(i))
        }

        var haySumidero = false
        var hayDeltaConCaracter = false
        var newDelta = ""
        for(i in 0..(m.globalStates.size-1)){
            for(j in 0..(theAlphabet.size-1)){

                hayDeltaConCaracter = false
                for(k in 0..(m.globalDeltas.size-1)){
                    var deltaData1 = m.globalDeltas.get(k).split('(').get(1).split('=')
                    var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                    if((deltaData2.get(0).equals(m.globalStates.get(i))) && (deltaData2.get(1).equals(theAlphabet.get(j)))){
                        hayDeltaConCaracter = true
                        break
                    }
                }

                if(!hayDeltaConCaracter){

                    if(!haySumidero){
                        result.globalStates.add("qS")
                        result.globalAcceptanceStates.add("qS")

                        var sumideroDelta = ""
                        for(a in 0..(theAlphabet.size-1)){
                            sumideroDelta = "delta(qS," + theAlphabet.get(a) + ")=qS"
                            result.globalDeltas.add(sumideroDelta)
                        }

                        haySumidero = true
                    }

                    newDelta = "delta("+m.globalStates.get(i)+","+theAlphabet.get(j)+")=qS"
                    result.globalDeltas.add(newDelta)
                }
            }
        }

        var isStateAcceptance = false
        for(p in 0..(m.globalStates.size-1)){

            isStateAcceptance = false
            for(a in 0..(m.globalAcceptanceStates.size-1)){
                if(m.globalAcceptanceStates.get(a).equals(m.globalStates.get(p))){
                    isStateAcceptance = true
                    break
                }
            }

            if(!isStateAcceptance){
                result.globalAcceptanceStates.add(m.globalStates.get(p))
            }
        }

        return result
    }

    fun minimizeAutomata(m: GlobalAutomata){
        var aa = findPossibleStrings(m,"C")
        for(a in 0..(aa.size-1)){
            println(aa.get(a))
        }
    }

    fun getRecursiveStringsFromPossibleStrings(possibleStrings: ArrayList<String>): ArrayList<String>{
        var result = ArrayList<String>()
        return result
    }

    fun getAcceptanceStringsFromPossibleStrings(possibleStrings: ArrayList<String>): ArrayList<String>{
        var result = ArrayList<String>()
        return result
    }

    fun generateAcceptanceTestStrings(recursiveStrings: ArrayList<String>,acceptanceStrings: ArrayList<String>): ArrayList<String>{
        var result = ArrayList<String>()
        return result
    }

    fun findPossibleStrings(m: GlobalAutomata, theState: String): ArrayList<String>{
        var result = ArrayList<String>()

        for(a in 0..(m.globalAcceptanceStates.size-1)){
            if(m.globalAcceptanceStates.get(a).equals(theState)){
                result.add(theState+"#E")
            }
        }

        var statesToProcess = ArrayList<String>()
        statesToProcess.add(theState+"#")

        var tempStates = ArrayList<String>()
        tempStates.add("dummy")

        var currentStates = ArrayList<String>()
        currentStates.add("dummy")

        var recursiveCharacters = ArrayList<String>()
        recursiveCharacters.add("dummy")
        var recursiveString: String
        var nonrecursiveCharacters = ArrayList<String>()
        nonrecursiveCharacters.add("dummy")

        var currentStatesWithoutSplit: String
        var currentState: String
        var currentString: String
        var newString: String
        var numberOfAppearancesOfState: Int

        while(tempStates.size>0){
            tempStates.clear()

            for(i in 0..(statesToProcess.size-1)){
                currentStatesWithoutSplit = statesToProcess.get(i).split('#').get(0)
                currentStates.clear()
                if(currentStatesWithoutSplit.contains('^')){
                    var xx = currentStatesWithoutSplit.split('^')
                    for(x in 0..(xx.size-1)){
                        currentStates.add(xx.get(x))
                    }
                    currentState = xx.get(xx.size-1)
                } else {
                    currentStates.add(currentStatesWithoutSplit)
                    currentState = currentStatesWithoutSplit
                }
                currentString = statesToProcess.get(i).split('#').get(1)

                var productionsFromState = getProductionsForOneState(m,currentState)
                recursiveCharacters.clear()
                nonrecursiveCharacters.clear()
                for(w in 0..(productionsFromState.size-1)){
                    if(productionsFromState.get(w).contains('*')){
                        recursiveCharacters.add(productionsFromState.get(w).split('@').get(0).split('*').get(0))
                    } else {
                        nonrecursiveCharacters.add(productionsFromState.get(w))
                    }
                }

                numberOfAppearancesOfState = 0
                for(q in 0..(currentStates.size-1)){
                    if(currentStates.get(q).equals(currentState)){
                        numberOfAppearancesOfState++
                    }
                }

                if(numberOfAppearancesOfState>1){
                    if(recursiveCharacters.size>0){
                        if(recursiveCharacters.size==1){
                            recursiveString = "("+recursiveCharacters.get(0)+")*"
                        } else {
                            recursiveString = "(" + recursiveCharacters.get(0)
                            for(t in 1..(recursiveCharacters.size-1)){
                                recursiveString = recursiveString + "+" + recursiveCharacters.get(t)
                            }
                            recursiveString = recursiveString + ")*"
                        }
                        result.add(currentStatesWithoutSplit+"#"+currentString+"%"+recursiveString)
                    } else {
                        result.add(currentStatesWithoutSplit+"#"+currentString)
                    }
                } else {
                    if(recursiveCharacters.size>0){
                        if(recursiveCharacters.size==1){
                            recursiveString = "("+recursiveCharacters.get(0)+")*"
                        } else {
                            recursiveString = "(" + recursiveCharacters.get(0)
                            for(t in 1..(recursiveCharacters.size-1)){
                                recursiveString = recursiveString + "+" + recursiveCharacters.get(t)
                            }
                            recursiveString = recursiveString + ")*"
                        }

                        for(p in 0..(nonrecursiveCharacters.size-1)){
                            var productionParts = nonrecursiveCharacters.get(p).split('@')
                            if(currentString.equals("")){
                                newString = recursiveString+productionParts.get(0)
                            } else {
                                newString = currentString+"%"+recursiveString+productionParts.get(0)
                            }
                            tempStates.add(currentStatesWithoutSplit+"^"+productionParts.get(1)+"#"+newString)
                        }
                    } else {
                        for(p in 0..(productionsFromState.size-1)){
                            var productionParts = productionsFromState.get(p).split('@')
                            if(currentString.equals("")){
                                newString = productionParts.get(0)
                            } else {
                                newString = currentString+"%"+productionParts.get(0)
                            }
                            tempStates.add(currentStatesWithoutSplit+"^"+productionParts.get(1)+"#"+newString)
                        }
                    }
                }
            }

            statesToProcess.clear()
            for(k in 0..(tempStates.size-1)){
                statesToProcess.add(tempStates.get(k))
            }
        }
        return result
    }

    fun getProductionsForOneState(m: GlobalAutomata, theState: String): ArrayList<String>{
        var result = ArrayList<String>()
        var newResult: String
        for(d in 0..(m.globalDeltas.size-1)){
            val currentDelta = m.globalDeltas.get(d)
            val deltaData1 = currentDelta.split('(').get(1).split(')')
            val deltaData2 = deltaData1.get(0).split(',')
            val deltaData3 = deltaData1.get(1).split('=').get(1)

            if(deltaData2.get(0).equals(theState)){
                newResult = deltaData2.get(1)+"@"+deltaData3
                if(theState.equals(deltaData3)){
                    newResult = deltaData2.get(1)+"*@"+deltaData3
                }
                result.add(newResult)
            }
        }
        return result
    }

    fun ER_To_NFAE(erString: String): GlobalAutomata{
        var NFAE = GlobalAutomata()
        NFAE.globalAutomataType = "NFA-E"

        var theAlphabet = ""
        for(i in 0..(erString.length-1)){
            if((!erString.get(i).equals('(')) &&
                    (!erString.get(i).equals(')')) &&
                    (!erString.get(i).equals('*')) &&
                    (!erString.get(i).equals('+')) &&
                    (!erString.get(i).equals('.'))){
                if(!theAlphabet.contains(erString.get(i))){
                    if(theAlphabet.equals("")){
                        theAlphabet = erString.get(i).toString()
                    } else {
                        theAlphabet = theAlphabet + "," + erString.get(i).toString()
                    }
                }
            }
        }

        var regExTree_RootNode = RegExTreeBuilder.RegExTreeBuilder().BuildRegExTree(erString)

        if(regExTree_RootNode.javaClass.name.split('.').get(2).equals("CharNode")){
            var theChar = printTree(regExTree_RootNode)
            //*****Construct NFAE**********
            //Construct NFAE for c
            NFAE.globalAlphabet = theAlphabet
            var statesCounter = 0
            var resultOperationChar = ER_operateChar(NFAE,theChar,statesCounter).split('@')
            NFAE.globalInitialState = resultOperationChar.get(0)
            NFAE.globalAcceptanceStates.add(resultOperationChar.get(1))
        } else if((regExTree_RootNode.javaClass.name.split('.').get(2).equals("RepeatNode")) &&
                ((regExTree_RootNode as RepeatNode).node.javaClass.name.split('.').get(2).equals("CharNode"))) {
            var repeatedChar = printTree(regExTree_RootNode.node)
            //*****Construct NFAE**********
            // Construct NFAE for c*
            NFAE.globalAlphabet = theAlphabet + ",E"
            var statesCounter = 0
            var resultOperationChar = ER_operateChar(NFAE,repeatedChar,statesCounter).split('@')
            statesCounter = resultOperationChar.get(2).toInt()
            var resultOperationKleene = ER_operateKleene(NFAE,resultOperationChar.get(0),resultOperationChar.get(1),statesCounter).split('@')
            NFAE.globalInitialState = resultOperationKleene.get(0)
            NFAE.globalAcceptanceStates.add(resultOperationKleene.get(1))
        } else {
            //*****Construct NFAE**********
            // Construct NFAE for ER
            NFAE.globalAlphabet = theAlphabet + ",E"

            var globalNumberOfStates = 0

            var erTreeString = printTree(regExTree_RootNode)
            var erPartsToProcess = ArrayList<String>()
            var erTreeStringPos = 0
            while(erTreeStringPos < erTreeString.length){
                if(erTreeString.get(erTreeStringPos).equals('*')){
                    erPartsToProcess.add(erTreeString.get(erTreeStringPos).toString()+erTreeString.get(erTreeStringPos+1).toString())
                    erTreeStringPos++
                    erTreeStringPos++
                } else {
                    erPartsToProcess.add(erTreeString.get(erTreeStringPos).toString())
                    erTreeStringPos++
                }
            }

            var erTempParts = ArrayList<String>()
            erTempParts.add("dummy1")
            erTempParts.add("dummy2")
            var partsToProcessPos = 0

            var currentPart = ""
            var currentPartOneAhead = ""
            var currentPartTwoAhead = ""
            var isPartOneAheadBondable = 0
            var isPartTwoAheadBondable = 0

            var partOneResultString = ""
            var partTwoResultString = ""
            var operatorResultString = ""

            while(erTempParts.size>1){
                erTempParts.clear()

                partsToProcessPos = 0
                while(partsToProcessPos < erPartsToProcess.size){

                    currentPart = erPartsToProcess.get(partsToProcessPos)
                    currentPartOneAhead = erPartsToProcess.get(partsToProcessPos+1)
                    currentPartTwoAhead = erPartsToProcess.get(partsToProcessPos+2)
                    isPartOneAheadBondable = 0
                    isPartTwoAheadBondable = 0

                    if(currentPartOneAhead.contains('@')){
                        isPartOneAheadBondable = 1
                    } else if((!currentPartOneAhead.get(currentPartOneAhead.length-1).equals('+')) && (!currentPartOneAhead.get(currentPartOneAhead.length-1).equals('.'))){
                        isPartOneAheadBondable = 2
                    }

                    if(currentPartTwoAhead.contains('@')){
                        isPartTwoAheadBondable = 1
                    } else if((!currentPartTwoAhead.get(currentPartTwoAhead.length-1).equals('+')) && (!currentPartTwoAhead.get(currentPartTwoAhead.length-1).equals('.'))){
                        isPartTwoAheadBondable = 2
                    }

                    if((isPartOneAheadBondable>0) && (isPartTwoAheadBondable>0)){

                        partOneResultString = ""
                        partTwoResultString = ""

                        if(isPartOneAheadBondable==1){
                            partOneResultString = currentPartOneAhead
                        } else if(isPartOneAheadBondable==2){
                            partOneResultString = ER_operateChar(NFAE,currentPartOneAhead.get(currentPartOneAhead.length-1).toString(),globalNumberOfStates)
                            globalNumberOfStates = partOneResultString.split('@').get(2).toInt()
                            if(currentPartOneAhead.get(0).equals('*')){
                                partOneResultString = ER_operateKleene(NFAE,partOneResultString.split('@').get(0),partOneResultString.split('@').get(1),globalNumberOfStates)
                                globalNumberOfStates = partOneResultString.split('@').get(2).toInt()
                            }
                        }

                        if(isPartTwoAheadBondable==1){
                            partTwoResultString = currentPartTwoAhead
                        } else if(isPartTwoAheadBondable==2){
                            partTwoResultString = ER_operateChar(NFAE,currentPartTwoAhead.get(currentPartTwoAhead.length-1).toString(),globalNumberOfStates)
                            globalNumberOfStates = partTwoResultString.split('@').get(2).toInt()
                            if(currentPartTwoAhead.get(0).equals('*')){
                                partTwoResultString = ER_operateKleene(NFAE,partTwoResultString.split('@').get(0),partTwoResultString.split('@').get(1),globalNumberOfStates)
                                globalNumberOfStates = partTwoResultString.split('@').get(2).toInt()
                            }
                        }

                        operatorResultString = ""
                        if(currentPart.contains('+')){
                            operatorResultString = ER_operateOR(NFAE,partOneResultString.split('@').get(0),partOneResultString.split('@').get(1),partTwoResultString.split('@').get(0),partTwoResultString.split('@').get(1),globalNumberOfStates)
                            globalNumberOfStates = operatorResultString.split('@').get(2).toInt()
                        } else if(currentPart.contains('.')){
                            operatorResultString = ER_operateAnd(NFAE,partOneResultString.split('@').get(0),partOneResultString.split('@').get(1),partTwoResultString.split('@').get(0),partTwoResultString.split('@').get(1))
                        }

                        if(currentPart.contains('*')){
                            operatorResultString = ER_operateKleene(NFAE,operatorResultString.split('@').get(0),operatorResultString.split('@').get(1),globalNumberOfStates)
                            globalNumberOfStates = operatorResultString.split('@').get(2).toInt()
                        }

                        erTempParts.add(operatorResultString.split('@').get(0)+"@"+operatorResultString.split('@').get(1))
                        partsToProcessPos++
                        partsToProcessPos++
                        partsToProcessPos++
                    } else {
                        erTempParts.add(currentPart)
                        partsToProcessPos++
                    }
                }

                erPartsToProcess.clear()
                for(i in 0..(erTempParts.size-1)){
                    erPartsToProcess.add(erTempParts.get(i))
                }
            }
            NFAE.globalInitialState = erPartsToProcess.get(0).split('@').get(0)
            NFAE.globalAcceptanceStates.add(erPartsToProcess.get(0).split('@').get(1))
        }
        return NFAE
    }

    fun printTree(rootNode: Any): String{
        if(rootNode.javaClass.name.split('.').get(2).equals("ORNode")){
            rootNode as ORNode
            return "+" + printTree(rootNode.LeftNode) + printTree(rootNode.RightNode)
        } else if(rootNode.javaClass.name.split('.').get(2).equals("RepeatNode")){
            rootNode as RepeatNode
            return "*" + printTree(rootNode.node)
        } else if(rootNode.javaClass.name.split('.').get(2).equals("ANDNode")){
            rootNode as ANDNode
            return "." + printTree(rootNode.LeftNode) + printTree(rootNode.RightNode)
        }  else{
            rootNode as CharNode
            return rootNode.inverseExpression()
        }
    }

    fun ER_operateChar(m: GlobalAutomata, char: String, currentCounter: Int): String{
        var currentNumberOfStates = currentCounter
        val firstState = "q"+currentNumberOfStates.toString()
        currentNumberOfStates++
        val secondState = "q"+currentNumberOfStates.toString()
        currentNumberOfStates++

        m.globalStates.add(firstState)
        m.globalStates.add(secondState)

        m.globalDeltas.add("delta("+firstState+","+char+")="+secondState)

        var result = firstState+"@"+secondState+"@"+currentNumberOfStates.toString()
        return result
    }

    fun ER_operateKleene(m: GlobalAutomata, initialState: String, finalState: String, currentCounter: Int): String{
        var currentNumberOfStates = currentCounter
        val firstState = "q"+currentNumberOfStates.toString()
        currentNumberOfStates++
        val secondState = "q"+currentNumberOfStates.toString()
        currentNumberOfStates++

        m.globalStates.add(firstState)
        m.globalStates.add(secondState)

        m.globalDeltas.add("delta("+firstState+",E)="+initialState)
        m.globalDeltas.add("delta("+firstState+",E)="+secondState)
        m.globalDeltas.add("delta("+finalState+",E)="+initialState)
        m.globalDeltas.add("delta("+finalState+",E)="+secondState)

        var result = firstState+"@"+secondState+"@"+currentNumberOfStates.toString()
        return result
    }

    fun ER_operateAnd(m: GlobalAutomata, initialStateLeft: String, finalStateLeft: String, initialStateRight: String, finalStateRight: String): String {
        m.globalDeltas.add("delta("+finalStateLeft+",E)="+initialStateRight)
        var result = initialStateLeft+"@"+finalStateRight
        return result
    }

    fun ER_operateOR(m:GlobalAutomata, initialStateLeft: String, finalStateLeft: String, initialStateRight: String, finalStateRight: String, currentCounter: Int): String {
        var currentNumberOfStates = currentCounter
        val firstState = "q"+currentNumberOfStates.toString()
        currentNumberOfStates++
        val secondState = "q"+currentNumberOfStates.toString()
        currentNumberOfStates++

        m.globalStates.add(firstState)
        m.globalStates.add(secondState)

        m.globalDeltas.add("delta("+firstState+",E)="+initialStateLeft)
        m.globalDeltas.add("delta("+firstState+",E)="+initialStateRight)
        m.globalDeltas.add("delta("+finalStateLeft+",E)="+secondState)
        m.globalDeltas.add("delta("+finalStateRight+",E)="+secondState)

        var result = firstState+"@"+secondState+"@"+currentNumberOfStates.toString()
        return result
    }

    fun CFG_To_PDA(g: ContextFreeGramar): GlobalAutomata{
        var PDA = GlobalAutomata()

        PDA.globalAutomataType = "PDA"

        PDA.globalInitialState = "q0"

        PDA.globalStates.add("q0")
        PDA.globalStates.add("q1")
        PDA.globalStates.add("q2")

        PDA.globalAcceptanceStates.add("q2")

        var pdaAlphabet = "E"
        for(i in 0..(g.terminals.size-1)){
            pdaAlphabet = pdaAlphabet + "," + g.terminals.get(i)
        }
        PDA.globalAlphabet = pdaAlphabet

        var pdaPileAlphabet = pdaAlphabet + ",Z"
        for(i in 0..(g.nonTerminals.size-1)){
            pdaPileAlphabet = pdaPileAlphabet + "," + g.nonTerminals.get(i)
        }
        PDA.globalGammaAlphabet = pdaPileAlphabet

        var newDelta: String
        newDelta = "delta(q0,E,Z)=(q1,"+g.startSymbol+"Z)"
        PDA.globalDeltas.add(newDelta)
        newDelta = "delta(q1,E,Z)=(q2,Z)"
        PDA.globalDeltas.add(newDelta)
        for(i in 0..(g.productions.size-1)){
            var productionParts = g.productions.get(i).split(',')
            newDelta = "delta(q1,E,"+productionParts.get(0)+")=(q1,"+productionParts.get(1)+")"
            PDA.globalDeltas.add(newDelta)
        }
        for(i in 0..(g.terminals.size-1)){
            newDelta = "delta(q1,"+g.terminals.get(i)+","+g.terminals.get(i)+")=(q1,E)"
            PDA.globalDeltas.add(newDelta)
        }

        return PDA
    }

}