/**
 * Created by Carlos Escobar on 7/23/2016.
 */

package TheGlobalAutomata

import MainWindowUtility.*

import java.util.ArrayList
import com.mxgraph.view.mxGraph
import javax.swing.JFileChooser

class GlobalAutomata {
    var globalAutomataType: String = "DFA"

    var globalStates: ArrayList<String> = ArrayList<String>()
    var globalInitialState: String = ""
    var globalAcceptanceStates: ArrayList<String> = ArrayList<String>()
    var globalAlphabet: String = ""
    var globalGammaAlphabet: String = ""
    var globalInitialStatePile: String = "Z"
    var globalBlankCursor: String = "B"
    var globalDeltas: ArrayList<String> = ArrayList<String>()

    var globalGraph: mxGraph = mxGraph()
    var globalXsAndYs: ArrayList<String> = ArrayList<String>()

    var globalChooser1: JFileChooser = JFileChooser("ArchivosGuardados")
    var globalChooser2: JFileChooser = JFileChooser("ArchivosGuardados")
}

fun main(args: Array<String>) {
    //var globalAutomata = GlobalAutomata()
    //MainUtility().renderMainFunction(globalAutomata)

    val s = "123456789"
    println(s.length)
    println(s[s.length-1])
}

