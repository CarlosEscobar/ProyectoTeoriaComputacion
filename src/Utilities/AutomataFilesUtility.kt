/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package FilesUtility

import TheGlobalAutomata.*
import GraphUtility.*
import MainWindowUtility.MainUtility

import java.io.*
import javax.swing.JTextField
import java.awt.Container

class AutomataFilesUtility{

    fun writeAutomataFile(c: Container, m: GlobalAutomata, automataName: String){
        var fileContent = ""

        if(m.globalAutomataType.equals("ER")){
            val regularExpresion = MainUtility().getComponentByName<JTextField>(c, "t1ERTextField")
            if(regularExpresion != null) {
                fileContent = regularExpresion.text
            }
        } else {

            var tempStates = ""
            for (i in 0..(m.globalStates.size - 1)) {
                if (i == 0) {
                    tempStates = m.globalStates.get(i)
                } else {
                    tempStates = tempStates + "," + m.globalStates.get(i)
                }
            }

            var tempAcceptanceStates = ""
            for (i in 0..(m.globalAcceptanceStates.size - 1)) {
                if (i == 0) {
                    tempAcceptanceStates = m.globalAcceptanceStates.get(i)
                } else {
                    tempAcceptanceStates = tempAcceptanceStates + "," + m.globalAcceptanceStates.get(i)
                }
            }

            fileContent = fileContent + "Estados:" + tempStates + "$"
            fileContent = fileContent + "EstadosAceptacion:" + tempAcceptanceStates + "$"
            fileContent = fileContent + "EstadoInicial:" + m.globalInitialState + "$"
            fileContent = fileContent + "Alfabeto:" + m.globalAlphabet + "$"
            fileContent = fileContent + "AlfabetoGamma:" + m.globalGammaAlphabet + "$"

            for (i in 0..(m.globalDeltas.size - 1)) {
                fileContent = fileContent + m.globalDeltas.get(i) + "$"
            }

            var XsAndYs = AutomataGraphUtility().constructXsAndYs(m)
            fileContent = fileContent + "XsAndYs:" + XsAndYs
        }

        var fileName = "ArchivosGuardados/" + m.globalAutomataType + "/" + automataName + ".txt"
        File(fileName).printWriter().use { out ->
            out.print(fileContent)
        }
    }

    fun readFile(filePath: String): String {
        var content = ""
        val file = File(filePath)
        var reader: FileReader? = null
        try {
            reader = FileReader(file)
            val chars = CharArray(file.length().toInt())
            reader.read(chars)
            content = String(chars)
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                reader.close()
            }
        }
        return content
    }

    fun readAutomataFile(filePath: String, automataType: String): GlobalAutomata {
        var automataFromFile = GlobalAutomata()
        val fileData = readFile(filePath)
        val fileParts = fileData.split('$')

        automataFromFile.globalAutomataType = automataType
        for(i in 0..(fileParts.size-1)){
            if(fileParts.get(i).contains(':')){

                val innerFileParts = fileParts.get(i).split(':')
                val innerPartType = innerFileParts.get(0)
                when (innerPartType) {
                    "Estados" -> setStates(innerFileParts,automataFromFile)
                    "EstadosAceptacion" -> setAcceptanceStates(innerFileParts,automataFromFile)
                    "EstadoInicial" -> setInitialState(innerFileParts,automataFromFile)
                    "Alfabeto" -> setAlphabet(innerFileParts,automataFromFile)
                    "AlfabetoGamma" -> setGammaAlphabet(innerFileParts,automataFromFile)
                    "XsAndYs" -> setXsAndYs(innerFileParts,automataFromFile)
                }

            }else if(fileParts.get(i).startsWith("delta(")){
                automataFromFile.globalDeltas.add(fileParts.get(i))
            }
        }
        return automataFromFile
    }

    fun setStates(data: List<String>, m: GlobalAutomata){
        if(data.size > 0){
            if(data.get(1).contains(',')){
                var fileStates = data.get(1).split(',')
                for(p in 0..(fileStates.size-1)){
                    m.globalStates.add(fileStates.get(p))
                }
            } else {
                m.globalStates.add(data.get(1))
            }
        }
    }

    fun setAcceptanceStates(data: List<String>, m: GlobalAutomata){
        if(data.size > 0){
            if(data.get(1).contains(',')){
                var fileAcceptanceStates = data.get(1).split(',')
                for(p in 0..(fileAcceptanceStates.size-1)){
                    m.globalAcceptanceStates.add(fileAcceptanceStates.get(p))
                }
            } else {
                m.globalAcceptanceStates.add(data.get(1))
            }
        }
    }

    fun setInitialState(data: List<String>, m: GlobalAutomata){
        if(data.size > 0){
            m.globalInitialState = data.get(1)
        }
    }

    fun setAlphabet(data: List<String>, m: GlobalAutomata){
        if(data.size > 0){
            m.globalAlphabet = data.get(1)
        }
    }

    fun setGammaAlphabet(data: List<String>, m: GlobalAutomata){
        if(data.size > 0){
            m.globalGammaAlphabet = data.get(1)
        }
    }

    fun setXsAndYs(data: List<String>, m: GlobalAutomata){
        if(data.size > 0){
            if(data.get(1).contains('#')){
                var fileDeltas = data.get(1).split('#')
                for(p in 0..(fileDeltas.size-1)){
                    m.globalXsAndYs.add(fileDeltas.get(p))
                }
            } else {
                m.globalXsAndYs.add(data.get(1))
            }
        }
    }

}