/**
 * Created by Carlos Escobar on 7/31/2016.
 */

package TabRenderer

import TabLogic.*

import TheGlobalAutomata.*
import java.awt.Container
import java.awt.event.*
import java.util.*
import javax.swing.*

class TabPanelRenderer{

    fun renderTab1(c: Container, tabPanel: JPanel, m: GlobalAutomata){
        //Tab1: Automata
        tabPanel.layout = null
        tabPanel.name = "tab1Panel"
        tabPanel.setBounds(0,0,400,200)

        val t1NameLabel = JLabel()
        t1NameLabel.name = "t1NameLabel"
        t1NameLabel.text = "Nombre Automata"
        tabPanel.add(t1NameLabel)
        t1NameLabel.setBounds(30,15,130,25)

        val t1NameTextField = JTextField(30)
        t1NameTextField.name = "t1NameTextField"
        tabPanel.add(t1NameTextField)
        t1NameTextField.setBounds(180,15,180,25)

        val t1TypeLabel = JLabel()
        t1TypeLabel.name = "t1TypeLabel"
        t1TypeLabel.text = "Tipo Automata"
        tabPanel.add(t1TypeLabel)
        t1TypeLabel.setBounds(30,55,130,25)

        val t1TypesComboBox = JComboBox<String>()
        t1TypesComboBox.name = "t1TypesComboBox"
        t1TypesComboBox.addItem("DFA")
        t1TypesComboBox.addItem("NFA")
        t1TypesComboBox.addItem("NFA-E")
        t1TypesComboBox.addItem("ER")
        t1TypesComboBox.addItem("PDA")
        t1TypesComboBox.addItem("Maquina Turing")
        tabPanel.add(t1TypesComboBox)
        t1TypesComboBox.setBounds(180,55,180,25)
        t1TypesComboBox.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                TabPanelLogic().onChangeEventAutomataType(c,m)
            }
        })

        val t1ERLabel = JLabel()
        t1ERLabel.name = "t1ERLabel"
        t1ERLabel.text = "Expresion Regular"
        t1ERLabel.isVisible = false
        tabPanel.add(t1ERLabel)
        t1ERLabel.setBounds(30,95,180,25)

        val t1ERTextField = JTextField(40)
        t1ERTextField.name = "t1ERTextField"
        t1ERTextField.isVisible = false
        tabPanel.add(t1ERTextField)
        t1ERTextField.setBounds(180,95,180,25)

        val t1SaveButton = JButton("Guardar")
        t1SaveButton.name = "t1AddButton"
        tabPanel.add(t1SaveButton)
        t1SaveButton.setBounds(60,135,100,25)
        t1SaveButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().saveAutomata(c,m)
            }
        })

        val t1ClearButton = JButton("Suprimir")
        t1ClearButton.name = "t1ClearButton"
        tabPanel.add(t1ClearButton)
        t1ClearButton.setBounds(230,135,100,25)
        t1ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearAutomata(c,m)
            }
        })
    }

    fun renderTab2(frame: JFrame, c: Container, panel: JPanel, tabPanel: JPanel, m: GlobalAutomata){
        //Tab2: Nuevo Estado
        tabPanel.layout = null
        tabPanel.name = "tab2Panel"
        tabPanel.setBounds(0,0,400,200)

        val t2NameLabel = JLabel()
        t2NameLabel.name = "t2NameLabel"
        t2NameLabel.text = "Nombre"
        tabPanel.add(t2NameLabel)
        t2NameLabel.setBounds(30,30,130,25)

        val t2NameTextField = JTextField(20)
        t2NameTextField.name = "t2NameTextField"
        tabPanel.add(t2NameTextField)
        t2NameTextField.setBounds(180,30,180,25)

        val t2AcceptanceCheckBox = JCheckBox()
        t2AcceptanceCheckBox.name = "t2AcceptanceCheckBox"
        tabPanel.add(t2AcceptanceCheckBox)
        t2AcceptanceCheckBox.text = "Es Aceptacion?"
        t2AcceptanceCheckBox.setBounds(30,60,200,35)

        val t2AddButton = JButton("Agregar")
        t2AddButton.name = "t2AddButton"
        tabPanel.add(t2AddButton)
        t2AddButton.setBounds(60,120,100,25)
        t2AddButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().addNewState(frame,c,panel,m)
            }
        })

        val t2ClearButton = JButton("Suprimir")
        t2ClearButton.name = "t2ClearButton"
        tabPanel.add(t2ClearButton)
        t2ClearButton.setBounds(230,120,100,25)
        t2ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearNewState(c)
            }
        })
    }

    fun renderTab3(c: Container, tabPanel: JPanel, m: GlobalAutomata){
        //Tab3: Alfabeto
        tabPanel.layout = null
        tabPanel.name = "tab3Panel"
        tabPanel.setBounds(0,0,400,200)

        val t3NewCharacterLabel = JLabel()
        t3NewCharacterLabel.name = "t3NewCharacterLabel"
        t3NewCharacterLabel.text = "Caracter Nuevo"
        tabPanel.add(t3NewCharacterLabel)
        t3NewCharacterLabel.setBounds(30,25,130,25)

        val t3NewCharacterTextField = JTextField(20)
        t3NewCharacterTextField.name = "t3NewCharacterTextField"
        tabPanel.add(t3NewCharacterTextField)
        t3NewCharacterTextField.setBounds(180,25,180,25)

        val t3AlphabetLabel = JLabel()
        t3AlphabetLabel.name = "t3AlphabetLabel"
        t3AlphabetLabel.text = "Alfabeto"
        tabPanel.add(t3AlphabetLabel)
        t3AlphabetLabel.setBounds(30,70,130,25)

        val t3AlphabetTextField = JTextField(20)
        t3AlphabetTextField.name = "t3AlphabetTextField"
        tabPanel.add(t3AlphabetTextField)
        t3AlphabetTextField.setBounds(180,70,180,25)

        val t3AddButton = JButton("Agregar")
        t3AddButton.name = "t3AddButton"
        tabPanel.add(t3AddButton)
        t3AddButton.setBounds(60,120,100,25)
        t3AddButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().addCharacterToAlphabet(c,m)
            }
        })

        val t3ClearButton = JButton("Suprimir")
        t3ClearButton.name = "t3ClearButton"
        tabPanel.add(t3ClearButton)
        t3ClearButton.setBounds(230,120,100,25)
        t3ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearAlphabet(c,m)
            }
        })
    }

    fun renderTab4(c: Container, tabPanel: JPanel, m: GlobalAutomata){
        //Tab4: Alfabeto(Pila/Cinta)
        tabPanel.layout = null
        tabPanel.name = "tab4Panel"
        tabPanel.setBounds(0,0,400,200)

        val t4NewCharacterLabel = JLabel()
        t4NewCharacterLabel.name = "t4NewCharacterLabel"
        t4NewCharacterLabel.text = "Caracter Nuevo"
        tabPanel.add(t4NewCharacterLabel)
        t4NewCharacterLabel.setBounds(30,25,130,25)

        val t4NewCharacterTextField = JTextField(20)
        t4NewCharacterTextField.name = "t4NewCharacterTextField"
        tabPanel.add(t4NewCharacterTextField)
        t4NewCharacterTextField.setBounds(180,25,180,25)

        val t4AlphabetLabel = JLabel()
        t4AlphabetLabel.name = "t4AlphabetLabel"
        t4AlphabetLabel.text = "Alfabeto (Pila/Cinta)"
        tabPanel.add(t4AlphabetLabel)
        t4AlphabetLabel.setBounds(30,70,130,25)

        val t4AlphabetTextField = JTextField(20)
        t4AlphabetTextField.name = "t4AlphabetTextField"
        tabPanel.add(t4AlphabetTextField)
        t4AlphabetTextField.setBounds(180,70,180,25)

        val t4AddButton = JButton("Agregar")
        t4AddButton.name = "t4AddButton"
        tabPanel.add(t4AddButton)
        t4AddButton.setBounds(60,120,100,25)
        t4AddButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().addCharacterToGammaAlphabet(c,m)
            }
        })

        val t4ClearButton = JButton("Suprimir")
        t4ClearButton.name = "t4ClearButton"
        tabPanel.add(t4ClearButton)
        t4ClearButton.setBounds(230,120,100,25)
        t4ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearGammaAlphabet(c,m)
            }
        })
    }

    fun renderTab5(frame: JFrame, c: Container, panel: JPanel, tabPanel: JPanel, m: GlobalAutomata){
        //Tab5: Crear Arista
        tabPanel.layout = null
        tabPanel.name = "tab5Panel"
        tabPanel.setBounds(0,0,400,200)

        val t5FirstStateLabel = JLabel()
        t5FirstStateLabel.name = "t5FirstStateLabel"
        t5FirstStateLabel.text = "Estado Inicial"
        tabPanel.add(t5FirstStateLabel)
        t5FirstStateLabel.setBounds(30,15,130,25)

        val t5FirstStateTextField = JTextField(20)
        t5FirstStateTextField.name = "t5FirstStateTextField"
        tabPanel.add(t5FirstStateTextField)
        t5FirstStateTextField.setBounds(180,15,180,25)

        val t5LastStateLabel = JLabel()
        t5LastStateLabel.name = "t5LastStateLabel"
        t5LastStateLabel.text = "Estado Final"
        tabPanel.add(t5LastStateLabel)
        t5LastStateLabel.setBounds(30,55,130,25)

        val t5LastStateTextField = JTextField(20)
        t5LastStateTextField.name = "t5LastStateTextField"
        tabPanel.add(t5LastStateTextField)
        t5LastStateTextField.setBounds(180,55,180,25)

        val t5CharacterLabel = JLabel()
        t5CharacterLabel.name = "t5CharacterLabel"
        t5CharacterLabel.text = "Caracter Transicion"
        tabPanel.add(t5CharacterLabel)
        t5CharacterLabel.setBounds(30,95,180,25)

        val t5CharacterTextField = JTextField(20)
        t5CharacterTextField.name = "t5CharacterTextField"
        tabPanel.add(t5CharacterTextField)
        t5CharacterTextField.setBounds(180,95,180,25)

        val t5AddButton = JButton("Agregar")
        t5AddButton.name = "t5AddButton"
        tabPanel.add(t5AddButton)
        t5AddButton.setBounds(60,135,100,25)
        t5AddButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().addTransition(frame,c,panel,m)
            }
        })

        val t5ClearButton = JButton("Suprimir")
        t5ClearButton.name = "t5ClearButton"
        tabPanel.add(t5ClearButton)
        t5ClearButton.setBounds(230,135,100,25)
        t5ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearAddTransition(c)
            }
        })
    }

    fun renderTab6(frame: JFrame, c: Container, panel: JPanel, tabPanel: JPanel, m: GlobalAutomata){
        //Tab6: Acciones
        tabPanel.layout = null
        tabPanel.name = "tab6Panel"
        tabPanel.setBounds(0,0,400,200)

        val t6FirstAutomataLabel = JLabel()
        t6FirstAutomataLabel.name = "t6FirstAutomataLabel"
        t6FirstAutomataLabel.text = "Automata 1"
        tabPanel.add(t6FirstAutomataLabel)
        t6FirstAutomataLabel.setBounds(10,15,130,25)

        val t6FirstAutomataTextField = JTextField(20)
        t6FirstAutomataTextField.name = "t6FirstAutomataTextField"
        tabPanel.add(t6FirstAutomataTextField)
        t6FirstAutomataTextField.setBounds(90,15,170,25)

        val t6ChooseFileFirstAutomataButton = JButton("Choose File...")
        t6ChooseFileFirstAutomataButton.name = "t6ChooseFileFirstAutomataButton"
        tabPanel.add(t6ChooseFileFirstAutomataButton)
        t6ChooseFileFirstAutomataButton.setBounds(270,15,110,25)
        t6ChooseFileFirstAutomataButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().chooseFile1(c,m.globalChooser1)
            }
        })

        val t6SecondAutomataLabel = JLabel()
        t6SecondAutomataLabel.name = "t6SecondAutomataLabel"
        t6SecondAutomataLabel.text = "Automata 2"
        tabPanel.add(t6SecondAutomataLabel)
        t6SecondAutomataLabel.setBounds(10,55,130,25)

        val t6SecondAutomataTextField = JTextField(20)
        t6SecondAutomataTextField.name = "t6SecondAutomataTextField"
        tabPanel.add(t6SecondAutomataTextField)
        t6SecondAutomataTextField.setBounds(90,55,170,25)

        val t6ChooseFileSecondAutomataButton = JButton("Choose File...")
        t6ChooseFileSecondAutomataButton.name = "t6ChooseFileSecondAutomataButton"
        tabPanel.add(t6ChooseFileSecondAutomataButton)
        t6ChooseFileSecondAutomataButton.setBounds(270,55,110,25)
        t6ChooseFileSecondAutomataButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().chooseFile2(c,m.globalChooser2)
            }
        })

        val t6ActionLabel = JLabel()
        t6ActionLabel.name = "t6ActionLabel"
        t6ActionLabel.text = "Accion"
        tabPanel.add(t6ActionLabel)
        t6ActionLabel.setBounds(20,95,110,25)

        val t6ActionComboBox = JComboBox<String>()
        t6ActionComboBox.name = "t6ActionComboBox"
        t6ActionComboBox.addItem("--------Conversiones--------")
        t6ActionComboBox.addItem("<< Origen: DFA >>")
        t6ActionComboBox.addItem(" - Convertir DFA-ER")
        t6ActionComboBox.addItem("<< Origen: NFA >>")
        t6ActionComboBox.addItem(" - Convertir NFA-DFA")
        t6ActionComboBox.addItem(" - Convertir NFA-ER")
        t6ActionComboBox.addItem("<< Origen: NFAE >>")
        t6ActionComboBox.addItem(" - Convertir NFAE-NFA")
        t6ActionComboBox.addItem(" - Convertir NFAE-DFA")
        t6ActionComboBox.addItem(" - Convertir NFAE-ER")
        t6ActionComboBox.addItem("<< Origen: ER >>")
        t6ActionComboBox.addItem(" - Convertir ER-NFAE")
        t6ActionComboBox.addItem(" - Convertir ER-NFA")
        t6ActionComboBox.addItem(" - Convertir ER-DFA")
        t6ActionComboBox.addItem("<< Origen: Gramatica >>")
        t6ActionComboBox.addItem(" - Convertir Gramatica-PDA")
        t6ActionComboBox.addItem("------------------------------------")
        t6ActionComboBox.addItem("------------Acciones-----------")
        t6ActionComboBox.addItem("<< 1 Automata >>")
        t6ActionComboBox.addItem(" - Cargar Automata")
        t6ActionComboBox.addItem(" - Complemento Automata")
        t6ActionComboBox.addItem(" - Minimizacion Automata")
        t6ActionComboBox.addItem("<< 2 Automata >>")
        t6ActionComboBox.addItem(" - Union Automatas")
        t6ActionComboBox.addItem(" - Interseccion Automatas")
        t6ActionComboBox.addItem(" - Diferencia Automatas")
        t6ActionComboBox.addItem("------------------------------------")

        tabPanel.add(t6ActionComboBox)
        t6ActionComboBox.setBounds(90,95,170,25)

        val t6ExecuteButton = JButton("Ejecutar")
        t6ExecuteButton.name = "t6ExecuteButton"
        tabPanel.add(t6ExecuteButton)
        t6ExecuteButton.setBounds(70,135,100,25)
        t6ExecuteButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().executeAction(frame,c,panel,m)
            }
        })

        val t6ClearButton = JButton("Suprimir")
        t6ClearButton.name = "t6ClearButton"
        tabPanel.add(t6ClearButton)
        t6ClearButton.setBounds(240,135,100,25)
        t6ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearActions(c)
            }
        })
    }

    fun renderTab7( c: Container,tabPanel: JPanel, m: GlobalAutomata){
        //Tab7: Evaluar
        tabPanel.layout = null
        tabPanel.name = "tab7Panel"
        tabPanel.setBounds(0,0,400,200)

        val t7StringLabel = JLabel()
        t7StringLabel.name = "t7StringLabel"
        t7StringLabel.text = "Cadena"
        tabPanel.add(t7StringLabel)
        t7StringLabel.setBounds(30,20,130,25)

        val t7StringTextField = JTextField(20)
        t7StringTextField.name = "t7StringTextField"
        tabPanel.add(t7StringTextField)
        t7StringTextField.setBounds(140,20,200,25)

        val t7ResultLabel = JLabel()
        t7ResultLabel.name = "t7ResultLabel"
        t7ResultLabel.text = ""
        t7ResultLabel.setFont(t7ResultLabel.getFont().deriveFont(20.0f))
        tabPanel.add(t7ResultLabel)
        t7ResultLabel.setBounds(150,55,200,25)

        val t7TapeLabel = JLabel()
        t7TapeLabel.name = "t7TapeLabel"
        t7TapeLabel.text = ""
        tabPanel.add(t7TapeLabel)
        t7TapeLabel.setBounds(30,85,300,25)

        val t7EvaluateButton = JButton("Evaluar")
        t7EvaluateButton.name = "t7EvaluateButton"
        tabPanel.add(t7EvaluateButton)
        t7EvaluateButton.setBounds(60,120,100,25)
        t7EvaluateButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().evaluate(c,m)
            }
        })

        val t7ClearButton = JButton("Suprimir")
        t7ClearButton.name = "t7ClearButton"
        tabPanel.add(t7ClearButton)
        t7ClearButton.setBounds(230,120,100,25)
        t7ClearButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                TabPanelLogic().clearEvaluate(c)
            }
        })
    }

    fun renderTab8(tabPanel: JPanel){
        //Tab8: Ayuda
        tabPanel.layout = null
        tabPanel.name = "tab8Panel"
        tabPanel.setBounds(0,0,400,200)

        val helpLabels = ArrayList<String>()
        helpLabels.add("- Cuando se cambia el tipo de automata, se suprime el automata.")
        helpLabels.add("- Estados de automatas regulares no pueden llevar: '  /  .  ,  |  '")
        helpLabels.add("- Caracteres del alfabeto(pila/cinta) deben estar en el alfabeto.")
        helpLabels.add("- Caracteres de alfabetos no pueden llevar: '  /  .  ,  |  '")
        helpLabels.add("- Acciones suprimen automata actual antes de empezar.")
        helpLabels.add("- Acciones despliegan resultado pero no lo guardan.")

        var tempLabel: JLabel
        var tempY: Int
        for(i in 0..(helpLabels.size-1)){
            tempLabel = JLabel()
            tempLabel.text = helpLabels.get(i)

            tabPanel.add(tempLabel)

            tempY = 10 + (i*25)
            tempLabel.setBounds(10,tempY,380,20)
        }
    }

    fun renderTabPane(frame: JFrame, c: Container, panel: JPanel, m: GlobalAutomata) {
        panel.layout = null
        panel.name = "tabContainer"
        panel.setBounds(0,0,443,243)
        panel.background = java.awt.Color.decode("#006CE0")

        //Declare tab panels
        val tab1 = JPanel()
        val tab2 = JPanel()
        val tab3 = JPanel()
        val tab4 = JPanel()
        val tab5 = JPanel()
        val tab6 = JPanel()
        val tab7 = JPanel()
        val tab8 = JPanel()

        //Render the tab panels
        renderTab1(c,tab1,m)
        renderTab2(frame,c,panel,tab2,m)
        renderTab3(c,tab3,m)
        renderTab4(c,tab4,m)
        renderTab5(frame,c,panel,tab5,m)
        renderTab6(frame,c,panel,tab6,m)
        renderTab7(c,tab7,m)
        renderTab8(tab8)

        //Construct the tab panel
        val jtp = JTabbedPane()
        jtp.name = "tabbedPanel"

        jtp.addTab("Automata", tab1)
        jtp.addTab("Nuevo Estado", tab2)
        jtp.addTab("Alfabeto", tab3)
        jtp.addTab("Alfabeto(Pila/Cinta)", tab4)
        jtp.addTab("Crear Arista", tab5)
        jtp.addTab("Evaluar", tab7)
        jtp.addTab("Acciones", tab6)
        jtp.addTab("Ayuda", tab8)

        jtp.tabLayoutPolicy = JTabbedPane.SCROLL_TAB_LAYOUT
        panel.add(jtp)
        jtp.setBounds(20,20,400,200)
    }

}