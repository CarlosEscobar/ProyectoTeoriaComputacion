/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package GraphUtility

import TheGlobalAutomata.*
import DeltaRenderer.*
import com.mxgraph.model.*

import com.mxgraph.swing.mxGraphComponent
import java.awt.Container
import java.awt.event.*
import java.util.*
import javax.swing.*

class AutomataGraphUtility{

    fun getGraphComponentByName(componentId: String, m: GlobalAutomata): mxCell? {
        val allGraphComponents = (m.globalGraph.model as mxGraphModel).cells
        for(i in 0..(allGraphComponents.size-1)){
            if(allGraphComponents.get(componentId) != null){
                return allGraphComponents.get(componentId) as mxCell
            }
        }
        return null
    }

    fun getGraphDeltaComponentsIds(m: GlobalAutomata): ArrayList<String>{
        var result = ArrayList<String>()
        val allGraphComponents = (m.globalGraph.model as mxGraphModel).cells.toList()
        for(i in 0..(allGraphComponents.size-1)){
            if(allGraphComponents[i] != null){
                val currentComp = allGraphComponents[i].second as mxCell
                if(currentComp.id.startsWith("delta",true)){
                    result.add(currentComp.id)
                }
            }
        }
        return result
    }

    fun createXsAndYsForStatesOnly(m: GlobalAutomata){
        var result = ArrayList<String>()
        val h = 415.0
        val k = 300.0
        val radius = 220.0
        val theta = 180.0
        val deltaTheta = 360.0/m.globalStates.size

        val q0X = h + (radius*Math.cos(Math.toRadians(theta)))
        val q0Y = k + (radius*Math.sin(Math.toRadians(theta)))
        val q0XandY = m.globalInitialState + "," + q0X.toString() + "," + q0Y.toString()
        result.add(q0XandY)

        var innerThetaCounter = 1
        var innerTheta = 0.0
        var innerX = 0.0
        var innerY = 0.0
        var newString = ""
        for(i in 0..(m.globalStates.size-1)){
            if(!m.globalStates.get(i).equals(m.globalInitialState)){
                innerTheta = theta + (deltaTheta*innerThetaCounter)
                innerThetaCounter = innerThetaCounter + 1
                innerX = h + (radius*Math.cos(Math.toRadians(innerTheta)))
                innerY = k + (radius*Math.sin(Math.toRadians(innerTheta)))
                newString = m.globalStates.get(i) + "," + innerX.toString() + "," + innerY.toString()
                result.add(newString)
            }
        }

        m.globalXsAndYs = ArrayList<String>()
        m.globalXsAndYs = result
    }

    fun constructXsAndYs(m: GlobalAutomata): String{
        var allComponents = ArrayList<String>()

        val deltaComponents = getGraphDeltaComponentsIds(m)
        for(k in 0..(deltaComponents.size-1)){
            val data = getXandY(m,deltaComponents.get(k).toString())
            allComponents.add(data)
        }

        for(p in 0..(m.globalStates.size-1)){
            val data = getXandY(m,m.globalStates.get(p))
            allComponents.add(data)
        }

        var result = ""
        for(k in 0..(allComponents.size-1)){
            if(k==0){
                result = allComponents.get(k)
            } else {
                result = result + "#" + allComponents.get(k)
            }
        }
        return result
    }

    fun getXandY(m: GlobalAutomata, state: String): String{
        var result = ""
        val theVertex = getGraphComponentByName(state, m)
        if(theVertex != null){
            result = state + "," + theVertex.geometry.x.toString() + "," + theVertex.geometry.y.toString()
        }
        return result
    }

    fun setXandY(m: GlobalAutomata, state: String, sX: String, sY: String){
        val theVertex = getGraphComponentByName(state, m)
        if(theVertex != null){
            m.globalGraph.model.beginUpdate()
            try {
                theVertex.geometry.setX(sX.toDouble())
                theVertex.geometry.setY(sY.toDouble())
            } finally {
                m.globalGraph.refresh()
                m.globalGraph.model.endUpdate()
            }
        }
    }

    fun drawCircle(frame: JFrame, c: Container, tabPanel: JPanel, stateName: String, isAcceptanceState: Boolean, m: GlobalAutomata){

        val parent = m.globalGraph.defaultParent
        m.globalGraph.model.beginUpdate()
        try {
            var stateShapeString = "shape=ellipse;fillColor=white"
            if(isAcceptanceState){
                stateShapeString = "shape=doubleEllipse;fillColor=white"
            }

            if(m.globalStates.size == 1){
                val zeroV = m.globalGraph.insertVertex(parent, "beforeQ0", "", 25.0, 300.0, 0.0, 0.0, "shape=ellipse;fillColor=white")
                val newV = m.globalGraph.insertVertex(parent, stateName, stateName, 100.0, 275.0, 50.0, 50.0, stateShapeString)
                m.globalGraph.insertEdge(parent, null, "", zeroV, newV)
            } else {
                m.globalGraph.insertVertex(parent, stateName, stateName, 150.0, 150.0, 50.0, 50.0, stateShapeString)
            }
        } finally {
            m.globalGraph.model.endUpdate()
        }

        m.globalGraph.isAllowDanglingEdges = false
        m.globalGraph.isEdgeLabelsMovable = false
        val graphComponent = mxGraphComponent(m.globalGraph)
        c.add(graphComponent)

        frame.isVisible = true

        graphComponent.setBounds(443,243,830,600)
        c.setComponentZOrder(tabPanel,0)
        c.setComponentZOrder(graphComponent,3)
    }

    fun drawTransition(frame: JFrame, c: Container, tabPanel: JPanel, firstState: String, lastState: String, m: GlobalAutomata){

        val parent = m.globalGraph.defaultParent
        m.globalGraph.model.beginUpdate()
        try {

            val vertexOne = getGraphComponentByName(firstState, m)
            val vertexTwo = getGraphComponentByName(lastState, m)

            if((vertexOne!=null) && (vertexTwo!=null)){

                val currentAutomataType = m.globalAutomataType
                var doesEdgeExist = false

                when (currentAutomataType) {
                    "DFA" -> doesEdgeExist = doesEdgeExistDFA(m,firstState,lastState)
                    "NFA" -> doesEdgeExist = doesEdgeExistNFA(m,firstState,lastState)
                    "NFA-E" -> doesEdgeExist = doesEdgeExistNFAE(m,firstState, lastState)
                    "PDA" -> doesEdgeExist = doesEdgeExistPDA(m,firstState, lastState)
                    "Maquina Turing" -> doesEdgeExist = doesEdgeExistTuring(m,firstState,lastState)
                }

                if(!doesEdgeExist){
                    if(firstState.equals(lastState)){
                        val deltaX = -30.0
                        val deltaY = 30.0
                        val transitionVertexName = "delta_"+firstState+"_"+lastState

                        val transitionV = m.globalGraph.insertVertex(parent, transitionVertexName, "",vertexOne.geometry.x+deltaX, vertexOne.geometry.y+deltaY, 18.0, 18.0, "shape=ellipse;fillColor=#007BFF")

                        m.globalGraph.insertEdge(parent, null, "", vertexOne, transitionV)
                        m.globalGraph.insertEdge(parent, null, "", transitionV, vertexOne)
                    } else {

                        val deltaX = ((vertexTwo.geometry.x - vertexOne.geometry.x)/2)
                        val deltaY = ((vertexTwo.geometry.y - vertexOne.geometry.y)/2)
                        val transitionVertexName = "delta_"+firstState+"_"+lastState

                        val transitionV = m.globalGraph.insertVertex(parent, transitionVertexName, "",vertexOne.geometry.x+deltaX, vertexOne.geometry.y+deltaY, 18.0, 18.0, "shape=ellipse;fillColor=#007BFF")

                        m.globalGraph.insertEdge(parent, null, "", vertexOne, transitionV)
                        m.globalGraph.insertEdge(parent, null, "", transitionV, vertexTwo)
                    }
                }
            }

        } finally {
            m.globalGraph.model.endUpdate()
        }

        m.globalGraph.isAllowDanglingEdges = false
        m.globalGraph.isEdgeLabelsMovable = false
        val graphComponent = mxGraphComponent(m.globalGraph)

        graphComponent.graphControl.addMouseListener(object : MouseAdapter() {
            override
            fun mouseClicked(e: MouseEvent) {
                val cell = graphComponent.getCellAt(e.x, e.y) as mxCell?
                if (cell != null) {
                    if(cell.id != null) {
                        val cellIdList = cell.id.toString().split('_')
                        if (cellIdList.size > 0) {
                            if (cellIdList.get(0).toString().equals("delta")) {
                                DeltaPanelRenderer().renderDeltaData(c,cellIdList.get(1).toString(),cellIdList.get(2).toString(),m)
                            }
                        }
                    }
                }
            }
        })

        c.add(graphComponent)

        frame.isVisible = true

        graphComponent.setBounds(443,243,830,600)
        c.setComponentZOrder(tabPanel,0)
        c.setComponentZOrder(graphComponent,3)
    }

    fun doesEdgeExistDFA(m: GlobalAutomata, initialState: String, finalState: String): Boolean{
        var result = false
        for(i in (0..(m.globalDeltas.size-1))){
            var currentDelta = m.globalDeltas.get(i)
            var part1 = currentDelta.split(',')
            var state1 = part1.get(0).split('(').get(1)
            var state2 = part1.get(1).split('=').get(1)

            if((state1.equals(initialState)) && (state2.equals(finalState))){
                result = true
                break
            }
        }
        return result
    }

    fun doesEdgeExistNFA(m: GlobalAutomata, initialState: String, finalState: String): Boolean{
        var result = false
        for(i in (0..(m.globalDeltas.size-1))){
            var currentDelta = m.globalDeltas.get(i)
            var part1 = currentDelta.split(',')
            var state1 = part1.get(0).split('(').get(1)
            var state2 = part1.get(1).split('=').get(1)

            if((state1.equals(initialState)) && (state2.equals(finalState))){
                result = true
                break
            }
        }
        return result
    }

    fun doesEdgeExistNFAE(m: GlobalAutomata, initialState: String, finalState: String): Boolean{
        var result = false
        for(i in (0..(m.globalDeltas.size-1))){
            var currentDelta = m.globalDeltas.get(i)
            var part1 = currentDelta.split(',')
            var state1 = part1.get(0).split('(').get(1)
            var state2 = part1.get(1).split('=').get(1)

            if((state1.equals(initialState)) && (state2.equals(finalState))){
                result = true
                break
            }
        }
        return result
    }

    fun doesEdgeExistPDA(m: GlobalAutomata, initialState: String, finalState: String): Boolean{
        var result = false
        for(i in (0..(m.globalDeltas.size-1))){
            var currentDelta = m.globalDeltas.get(i)
            var part1 = currentDelta.split('=')
            var state1 = part1.get(0).split('(').get(1).split(',').get(0)
            var state2 = part1.get(1).split('(').get(1).split(',').get(0)

            if((state1.equals(initialState)) && (state2.equals(finalState))){
                result = true
                break
            }
        }
        return result
    }

    fun doesEdgeExistTuring(m: GlobalAutomata, initialState: String, finalState: String): Boolean{
        var result = false
        for(i in (0..(m.globalDeltas.size-1))){
            var currentDelta = m.globalDeltas.get(i)
            var part1 = currentDelta.split('=')
            var state1 = part1.get(0).split('(').get(1).split(',').get(0)
            var state2 = part1.get(1).split('(').get(1).split(',').get(0)

            if((state1.equals(initialState)) && (state2.equals(finalState))){
                result = true
                break
            }
        }
        return result
    }

}