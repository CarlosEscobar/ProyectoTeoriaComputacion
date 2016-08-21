/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package OperationsAndActions

import FilesUtility.*
import TheGlobalAutomata.*
import MainWindowUtility.*

import java.awt.Container
import javax.swing.*
import java.util.*

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
                " - Convertir ER-NFAE" -> todo = 2.1
                " - Convertir ER-NFA" -> todo = 2.2
                " - Convertir ER-DFA" -> todo = 2.3
                " - Convertir Gramatica-PDA" -> todo = 3.1
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
        MainUtility().renderAutomataWithoutXsAndYs(frame,c,panel,m,minimizedAutomata,m.globalChooser1.selectedFile.name.split('.').get(0)+"_MIN")
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
                    if(considerAutomata1ToProcess){
                        for(d in 0..(automata1.globalDeltas.size-1)){
                            var deltaData1 = automata1.globalDeltas.get(d).split('(').get(1).split('=')
                            var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                            if((deltaData2.get(0).equals(automata1State)) && (deltaData2.get(1).equals(newAlphabetCharacters.get(a)))){
                                newPossibleState = deltaData1.get(1)
                                break
                            }
                        }
                    }
                    if(considerAutomata2ToProcess){
                        for(d in 0..(automata2.globalDeltas.size-1)){
                            var deltaData1 = automata2.globalDeltas.get(d).split('(').get(1).split('=')
                            var deltaData2 = deltaData1.get(0).split(')').get(0).split(',')
                            if((deltaData2.get(0).equals(automata2State)) && (deltaData2.get(1).equals(newAlphabetCharacters.get(a)))){
                                if(considerAutomata1ToProcess){
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

    // Usa . para unir estados
    fun minimizeAutomata(m: GlobalAutomata): GlobalAutomata{
        var result = GlobalAutomata()

        result.globalAlphabet = m.globalAlphabet

        //Crear tabla



        return result
    }

}