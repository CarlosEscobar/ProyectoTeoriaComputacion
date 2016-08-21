/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package DeltaLogic

import MainWindowUtility.*

import java.awt.*
import javax.swing.*

class DeltaPanelLogic{

    fun setDeltaPanelVisibility(c: Container, b: Boolean){
        val titleDeltaPanel = MainUtility().getComponentByName<JPanel>(c, "titleDeltaPanel")
        if(titleDeltaPanel != null) {
            titleDeltaPanel.isVisible = b
            c.setComponentZOrder(titleDeltaPanel,1)
        }

        val deltaPanel = MainUtility().getComponentByName<JPanel>(c, "deltaPanel")
        if(deltaPanel != null) {
            deltaPanel.isVisible = b
            c.setComponentZOrder(deltaPanel,2)
        }
    }

}