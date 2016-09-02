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

    // Usa . para unir estados
    fun minimizeAutomata(m: GlobalAutomata): GlobalAutomata{
        var result = GlobalAutomata()
        result.globalAlphabet = m.globalAlphabet

        var AllStatePairsEvaluated = ArrayList<String>()
        var AllStatePairsWithoutEvaluation = ArrayList<String>()

        //Crear tabla
        var statePairs = ArrayList<String>()
        var counter = 1
        for(i in 1..(m.globalStates.size-1)){
            for(j in 0..(counter-1)){
                statePairs.add(m.globalStates.get(j)+"$"+m.globalStates.get(i))
            }
            counter++
        }

        //Caso base para todos
        var isState1Acceptance:Boolean
        var isState2Acceptance:Boolean
        for(p in 0..(statePairs.size-1)){
            var currentStatePair = statePairs.get(p).split('$')
            isState1Acceptance = false
            isState2Acceptance = false

            for(k in 0..(m.globalAcceptanceStates.size-1)){
                if(m.globalAcceptanceStates.get(k).equals(currentStatePair.get(0))){
                    isState1Acceptance = true
                }
                if(m.globalAcceptanceStates.get(k).equals(currentStatePair.get(1))){
                    isState2Acceptance = true
                }
                if(isState1Acceptance && isState2Acceptance){
                    break
                }
            }

            if((isState1Acceptance && (!isState2Acceptance)) || ((!isState1Acceptance) && isState2Acceptance)){
                AllStatePairsWithoutEvaluation.add(currentStatePair.get(0)+"$"+currentStatePair.get(1))
                AllStatePairsEvaluated.add(currentStatePair.get(0)+"$"+currentStatePair.get(1)+"$"+"X")
            }
        }

        //Sacar parejas que no cumplieron el caso base
        var statesToEvaluate = ArrayList<String>()
        var isStatePairEvaluated:Boolean
        for(i in 0..(statePairs.size-1)){
            isStatePairEvaluated = false
            for(j in 0..(AllStatePairsWithoutEvaluation.size-1)){
                if(statePairs.get(i).equals(AllStatePairsWithoutEvaluation.get(j))){
                    isStatePairEvaluated = true
                    break
                }
            }

            if(!isStatePairEvaluated){
                statesToEvaluate.add(statePairs.get(i))
            }
        }

        //Evaluar cada caracter del alfabeto por pareja y evaluar caso de no devolver nada
        var theAlphabet = m.globalAlphabet.split(',')

        var StatePairsAlphabetEvaluationPairs = ArrayList<String>()
        var statePairAlphabetEvaluationToAdd: String

        var wasCaseFound: Boolean
        var tempStatesThatSatisfiedCase = ArrayList<String>()

        var deltaResultState1: String
        var deltaResultState2: String

        for(i in 0..(statesToEvaluate.size-1)){
            var currentStatePair = statesToEvaluate.get(i).split('$')
            wasCaseFound = false
            for(k in 0..(theAlphabet.size-1)){
                if(!wasCaseFound) {

                    deltaResultState1 = ""
                    deltaResultState2 = ""

                    for(d in 0..(m.globalDeltas.size-1)){
                        val currentDelta = m.globalDeltas.get(d)
                        val deltaData1 = currentDelta.split('(').get(1).split(')')
                        val deltaData2 = deltaData1.get(0).split(',')
                        val deltaData3 = deltaData1.get(1).split('=').get(1)

                        if((deltaData2.get(0).equals(currentStatePair.get(0))) && (deltaData2.get(1).equals(theAlphabet.get(k)))){
                            deltaResultState1 = deltaData3
                        }

                        if((deltaData2.get(0).equals(currentStatePair.get(1))) && (deltaData2.get(1).equals(theAlphabet.get(k)))){
                            deltaResultState2 = deltaData3
                        }
                    }

                    if((deltaResultState1 == "") || (deltaResultState2 == "")){
                        AllStatePairsWithoutEvaluation.add(currentStatePair.get(0)+"$"+currentStatePair.get(1))
                        tempStatesThatSatisfiedCase.add(currentStatePair.get(0)+"$"+currentStatePair.get(1))
                        AllStatePairsEvaluated.add(currentStatePair.get(0)+"$"+currentStatePair.get(1)+"$"+"X")
                        wasCaseFound = true
                    }

                    if(!wasCaseFound) {
                        statePairAlphabetEvaluationToAdd = currentStatePair.get(0) + "$" + currentStatePair.get(1) + "@" + theAlphabet.get(k) + "@" + deltaResultState1 + "$" + deltaResultState2
                        StatePairsAlphabetEvaluationPairs.add(statePairAlphabetEvaluationToAdd)
                    }
                }
            }
        }

        //Actualizar las parejas por evaluar, quitarles las que cumplieron el caso de no devolver nada
        for(p in 0..(tempStatesThatSatisfiedCase.size-1)){
            statesToEvaluate.remove(tempStatesThatSatisfiedCase.get(p))
        }

        //Buscar el caso de igualdad por devolver lo mismo en los estados por evaluar
        val theAlphabetSize = theAlphabet.size
        var stateSetReturnedTheSameCounter = 0
        var tempStatesFoundForCase = ArrayList<String>()
        var alphabetDeltaResultTemp: String

        for(i in 0..(statesToEvaluate.size-1)){
            var currentStatePair = statesToEvaluate.get(i).split('$')

            stateSetReturnedTheSameCounter = 0
            for(k in 0..(StatePairsAlphabetEvaluationPairs.size-1)){
                if(StatePairsAlphabetEvaluationPairs.get(k).split('@').get(0).equals(statesToEvaluate.get(i))){
                    alphabetDeltaResultTemp = StatePairsAlphabetEvaluationPairs.get(k).split('@').get(2)
                    var deltaResult = alphabetDeltaResultTemp.split('$')

                    if(deltaResult.get(0).equals(deltaResult.get(1))){
                        stateSetReturnedTheSameCounter++
                    }
                }
            }

            if(stateSetReturnedTheSameCounter == theAlphabetSize){
                AllStatePairsWithoutEvaluation.add(currentStatePair.get(0)+"$"+currentStatePair.get(1))
                tempStatesFoundForCase.add(currentStatePair.get(0)+"$"+currentStatePair.get(1))
                AllStatePairsEvaluated.add(currentStatePair.get(0)+"$"+currentStatePair.get(1)+"$"+"J")
            }
        }

        //Actualizar las parejas por evaluar, quitarles las que cumplieron el caso de todos caracteres devuelven lo mismo en la pareja
        var tempAlphabetEvaluations = ArrayList<String>()
        for(p in 0..(tempStatesFoundForCase.size-1)){
            statesToEvaluate.remove(tempStatesFoundForCase.get(p))

            for(k in 0..(StatePairsAlphabetEvaluationPairs.size-1)){
                if(StatePairsAlphabetEvaluationPairs.get(k).split('@').get(0).equals(tempStatesFoundForCase.get(p))){
                    tempAlphabetEvaluations.add(StatePairsAlphabetEvaluationPairs.get(k))
                }
            }
        }

        for(p in 0..(tempAlphabetEvaluations.size-1)){
            StatePairsAlphabetEvaluationPairs.remove(tempAlphabetEvaluations.get(p))
        }

        //Ciclo hasta evaluar todas las parejas
        var counterPerPairEquivalent = 0
        var counterPerPairNotEquivalent = 0
        var isCurrentPairEquivalent: Boolean?
        var atLeastOneFound = true

        while((AllStatePairsEvaluated.size != statePairs.size) && (atLeastOneFound)){

            var possibleStates = statesToEvaluate.subtract(AllStatePairsWithoutEvaluation).toList()
            atLeastOneFound = false
            for(i in 0..(possibleStates.size-1)){

                counterPerPairEquivalent = 0
                counterPerPairNotEquivalent = 0

                for(j in 0..(StatePairsAlphabetEvaluationPairs.size-1)){
                    if(StatePairsAlphabetEvaluationPairs.get(j).split('@').get(0).equals(possibleStates.get(i))){
                        var curStatePairAlphabet = StatePairsAlphabetEvaluationPairs.get(j).split('@').get(2)
                        var temp = curStatePairAlphabet.split('$')
                        var curStatePairAlphabet2 = temp.get(1)+"$"+temp.get(0)

                        isCurrentPairEquivalent = null

                        if(temp.get(0).equals(temp.get(1))){
                            isCurrentPairEquivalent = true
                        }

                        if(isCurrentPairEquivalent == null) {
                            for (k in 0..(AllStatePairsEvaluated.size - 1)) {

                                if (AllStatePairsEvaluated.get(k).contains(curStatePairAlphabet)) {
                                    if (AllStatePairsEvaluated.get(k).split('$').get(2).equals("J")) {
                                        isCurrentPairEquivalent = true
                                    }

                                    if (AllStatePairsEvaluated.get(k).split('$').get(2).equals("X")) {
                                        isCurrentPairEquivalent = false
                                    }
                                }

                                if (AllStatePairsEvaluated.get(k).contains(curStatePairAlphabet2)) {
                                    if (AllStatePairsEvaluated.get(k).split('$').get(2).equals("J")) {
                                        isCurrentPairEquivalent = true
                                    }

                                    if (AllStatePairsEvaluated.get(k).split('$').get(2).equals("X")) {
                                        isCurrentPairEquivalent = false
                                    }
                                }
                            }
                        }

                        if(isCurrentPairEquivalent != null){
                            if(isCurrentPairEquivalent){
                                counterPerPairEquivalent++
                            } else {
                                counterPerPairNotEquivalent++
                            }
                        }
                    }
                }

                if(counterPerPairNotEquivalent > 0){
                    atLeastOneFound = true
                    AllStatePairsWithoutEvaluation.add(possibleStates.get(i))
                    AllStatePairsEvaluated.add(possibleStates.get(i) + "$" + "X")
                }

                if(counterPerPairEquivalent == theAlphabetSize){
                    atLeastOneFound = true
                    AllStatePairsWithoutEvaluation.add(possibleStates.get(i))
                    AllStatePairsEvaluated.add(possibleStates.get(i)+"$"+"J")
                }
            }
        }

        //tabla evaluada completamente

        for(l in 0..(AllStatePairsEvaluated.size-1)){
            println(AllStatePairsEvaluated.get(l))
        }

        return result
    }

    fun findPossiblePaths(m: GlobalAutomata, initialState: String, finalState: String): ArrayList<String>{
        var result = ArrayList<String>()

        var tempData = ArrayList<String>()
        tempData.add(initialState)

        val worstCaseLength = m.globalStates.size * 2
        for(i in 1..(worstCaseLength)){

        }



        return result
    }

}