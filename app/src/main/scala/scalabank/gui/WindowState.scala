package scalabank.gui

import Monads.*
import States.*
import State.*
import scalabank.gui.SwingFunctionalFacade.Frame

import java.awt.{BorderLayout, FlowLayout, GridLayout, LayoutManager}
import java.util.function.Supplier

trait WindowState:
  type Window
  def initialWindow: Window
  def setSize(width: Int, height: Int): State[Window, Unit]
  def setMinSize(width: Int, height: Int): State[Window, Unit]
  def addView(name: String, layout: LayoutManager): State[Window, Unit]
  def showView(name: String): State[Window, Unit]
  def addPanel(name: String, layout: LayoutManager, view: String, constraints: Any): State[Window, Unit]
  def addButton(name: String, text: String, view: String, constraints: Any): State[Window, Unit]
  def addLabel(name: String, text: String, view: String, constraints: Any): State[Window, Unit]
  def changeLabel(name: String, text: String): State[Window, Unit]
  def addInput(name: String, columns: Int, panel: String, constraints: Any): State[Window, Unit]
  def getInputText(name: String): State[Window, String]
  def addComboBox(name: String, options: Array[String], panel: String, constraints: Any): State[Window, Unit]
  def getComboBoxSelection(name: String): State[Window, String]
  def addList(name: String, contents: Array[String], panel: String, constraints: Any): State[Window, Unit]

  def show(): State[Window, Unit]
  def exec(cmd: =>Unit): State[Window, Unit]
  def eventStream(): State[Window, LazyList[String]]

extension (so: LazyList.type)
  def generate[A](source: Supplier[A]): LazyList[A] = LazyList.iterate[A](source.get())(_ => source.get())


object WindowStateImpl extends WindowState:
  import SwingFunctionalFacade.*

  type Window = Frame

  def initialWindow: Window = createFrame

  def setSize(width: Int, height: Int): State[Window, Unit] =
    State(w => (w.setSize(width, height), {}))
  def setMinSize(width: Int, height: Int): State[Window, Unit] =
    State(w => (w.setMinSize(width, height), {}))
  def addView(name: String, layout: LayoutManager): State[Window, Unit] =
    State(w => (w.addView(name, layout), {}))
  def showView(name: String): State[Window, Unit] =
    State(w => (w.showView(name), {}))
  def addPanel(name: String, layout: LayoutManager, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addPanel(name, layout, panel, constraints), {}))
  def addButton(name: String, text: String, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addButton(name, text, panel, constraints), {}))
  def addLabel(name: String, text: String, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addLabel(name, text, panel, constraints), {}))
  def changeLabel(name: String, text: String): State[Window, Unit] =
    State(w => (w.changeLabel(name, text), {}))
  def addInput(name: String, columns: Int, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addInput(name, columns, panel, constraints), {}))
  def getInputText(name: String): State[Window, String] =
    State(w => (w, w.getInputText(name)))
  def addComboBox(name: String, options: Array[String], panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addComboBox(name, options, panel, constraints), {}))
  def getComboBoxSelection(name: String): State[Window, String] =
    State(w => (w, w.getComboBoxSelection(name)))
  def addList(name: String, contents: Array[String], panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addList(name, contents, panel, constraints), {}))
  def show(): State[Window, Unit] =
    State(w => (w.show, {}))
  def exec(cmd: =>Unit): State[Window, Unit] =
    State(w => (w, cmd))  
  def eventStream(): State[Window, LazyList[String]] =
    State(w => (w, LazyList.generate(w.events)))

@main def testView(): Unit =
  import WindowStateImpl.*
  import Monads.*, Monad.*
  val loginView = for
    _ <- setSize(600, 300)
    _ <- setMinSize(300, 100)
    // ------ Login View
    _ <- addView("Login", BorderLayout(0, 40))
    _ <- addPanel("Login-Title-Panel", FlowLayout(), "Login", BorderLayout.NORTH)
    _ <- addLabel("Login-Title", "Scalabank", "Login-Title-Panel", FlowLayout.CENTER)

    _ <- addPanel("Login-Inputs-Panel", GridLayout(2, 2), "Login", BorderLayout.CENTER)
    - <- addPanel("Name-Label-Panel", FlowLayout(), "Login-Inputs-Panel", null)
    _ <- addLabel("Name-Label", "Nome:", "Name-Label-Panel", FlowLayout.CENTER)
    _ <- addInput("Name-Input", 20, "Login-Inputs-Panel", null)
    - <- addPanel("Surname-Label-Panel", FlowLayout(), "Login-Inputs-Panel", null)
    _ <- addLabel("Surname-Label", "Cognome:", "Surname-Label-Panel", FlowLayout.CENTER)
    _ <- addInput("Surname-Input", 20, "Login-Inputs-Panel", null)

    _ <- addPanel("Login-Button-Panel", FlowLayout(), "Login", BorderLayout.SOUTH)
    _ <- addButton("Client-Login-Button", "Login come cliente", "Login-Button-Panel", FlowLayout.LEFT)
    _ <- addButton("Empl-Login-Button", "Login come dipendente", "Login-Button-Panel", FlowLayout.TRAILING)
  yield ()
  val userHomeView = for    // ------ User home View
    _ <- addView("User-Home", BorderLayout(0, 10))
    _ <- addPanel("User-Title-Panel", FlowLayout(), "User-Home", BorderLayout.NORTH)
    _ <- addLabel("User-Title", "Benvenuto <USER>", "User-Title-Panel", FlowLayout.CENTER)

    _ <- addPanel("User-Home-Panel", GridLayout(3, 2, 20, 20), "User-Home", BorderLayout.CENTER)
    _ <- addComboBox("User-Accounts", Array(), "User-Home-Panel", null)
    _ <- addButton("User-Home-Account", "Vedi conto", "User-Home-Panel", null)
    _ <- addButton("User-Home-Appointments", "Gestione appuntamenti", "User-Home-Panel", null)
    _ <- addButton("User-Home-New-Account", "Apri conto", "User-Home-Panel", null)
    _ <- addButton("User-Home-Sim-Loan", "Simulatore mutuo", "User-Home-Panel", null)
    _ <- addButton("User-Home-Logout", "Esci", "User-Home-Panel", null)
  yield ()
  val userAccountView = for     // ------ User account View
    _ <- addView("User-Account", BorderLayout(0, 10))
    _ <- addPanel("User-Account-Title-Panel", FlowLayout(), "User-Account", BorderLayout.NORTH)
    _ <- addLabel("User-Account-Title", "Conto ---", "User-Account-Title-Panel", FlowLayout.CENTER)
    _ <- addButton("User-Account-Back", "<", "User-Account", BorderLayout.WEST)

    _ <- addPanel("User-Account-Panel", GridLayout(3, 2, 20, 20), "User-Account", BorderLayout.CENTER)
    _ <- addPanel("Op-Amount-Panel", FlowLayout(), "User-Account-Panel", null)
    _ <- addLabel("Op-Amount-Label", "Importo:", "Op-Amount-Panel", FlowLayout.LEFT)
    _ <- addInput("Op-Amount", 10, "Op-Amount-Panel", FlowLayout.RIGHT)
    _ <- addButton("Account-Withdraw", "Prelievo", "User-Account-Panel", null)

    _ <- addPanel("Account-Amount-Panel", FlowLayout(), "User-Account-Panel", null)
    _ <- addLabel("Account-Amount", "Bilancio: ---", "Account-Amount-Panel", FlowLayout.CENTER)
    _ <- addButton("Account-Deposit", "Deposito", "User-Account-Panel", null)

    _ <- addPanel("Op-Target-Panel", FlowLayout(), "User-Account-Panel", null)
    _ <- addLabel("Op-Target-Label", "Destinatario:", "Op-Target-Panel", FlowLayout.LEFT)
    _ <- addInput("Op-Target", 10, "Op-Target-Panel", FlowLayout.RIGHT)
    _ <- addButton("Account-Transfer", "Bonifico", "User-Account-Panel", null)

    _ <- addPanel("Op-List-Panel", GridLayout(2, 2), "User-Account", BorderLayout.SOUTH)
    _ <- addPanel("Op-List-Label-Panel", FlowLayout(), "Op-List-Panel", null)
    _ <- addLabel("Op-List-Label", "Lista operazioni", "Op-List-Label-Panel", FlowLayout.CENTER)
    _ <- addList("Op-List", Array("uno", "due"), "Op-List-Panel", null)
  yield ()
  val userAppointmentsView = for     // ------ User appointments View
    _ <- addView("User-Appointments", BorderLayout(0, 10))
    _ <- addButton("User-Appts-Back", "<", "User-Appointments", BorderLayout.WEST)

    _ <- addPanel("User-Appt-Panel-Outer", FlowLayout(), "User-Appointments", BorderLayout.CENTER)
    _ <- addPanel("User-Appt-Panel", GridLayout(3, 1), "User-Appt-Panel-Outer", FlowLayout.CENTER)
    _ <- addPanel("Appt-Args-Panel", FlowLayout(), "User-Appt-Panel", null)
    _ <- addLabel("Appt-Args-Label", "Argomento:", "Appt-Args-Panel", FlowLayout.LEFT)
    _ <- addComboBox("Appt-Arguments", Array(), "Appt-Args-Panel", FlowLayout.RIGHT
    )
    _ <- addPanel("Appt-Date-Panel", FlowLayout(), "User-Appt-Panel", null)
    _ <- addLabel("Appt-Date-Label", "Data:", "Appt-Date-Panel", FlowLayout.LEFT)
    _ <- addInput("Appt-Date", 10, "Appt-Date-Panel", FlowLayout.RIGHT)
    _ <- addButton("Appt-Create", "Nuovo appuntamento", "User-Appt-Panel", null)

    _ <- addPanel("Appt-List-Panel", GridLayout(2, 2), "User-Appointments", BorderLayout.SOUTH)
    _ <- addPanel("Appt-List-Label-Panel", FlowLayout(), "Appt-List-Panel", null)
    _ <- addLabel("Appt-List-Label", "Lista appuntamenti", "Appt-List-Label-Panel", FlowLayout.CENTER)
    _ <- addList("Appt-List", Array("uno", "due"), "Appt-List-Panel", null)
  yield ()
  val userLoansView = for    // ------ User loans View
    _ <- addView("Loan-Simulator", BorderLayout(0, 10))
    _ <- addButton("Loan-Sim-Back", "<", "Loan-Simulator", BorderLayout.WEST)

    _ <- addPanel("Loan-Inputs", FlowLayout(), "Loan-Simulator", BorderLayout.NORTH)
    _ <- addLabel("Loan-Amount-Label", "Importo:", "Loan-Inputs", FlowLayout.LEFT)
    _ <- addInput("Loan-Amount", 10, "Loan-Inputs", FlowLayout.TRAILING)
    _ <- addLabel("Loan-Months-Label", "Numero mesi:", "Loan-Inputs", FlowLayout.TRAILING)
    _ <- addInput("Loan-Months", 10, "Loan-Inputs", FlowLayout.TRAILING)
    _ <- addButton("Loan-Calc", "Calcola", "Loan-Simulator", BorderLayout.CENTER)

    _ <- addPanel("Loan-Results-Outer", FlowLayout(), "Loan-Simulator", BorderLayout.SOUTH)
    _ <- addPanel("Loan-Results", GridLayout(2, 1), "Loan-Results-Outer", FlowLayout.CENTER)
    _ <- addLabel("Loan-Calc-Rate", "Rata mensile: ---", "Loan-Results", null)
    _ <- addLabel("Loan-Calc-Interest", "Ammontare interessi totale: ---", "Loan-Results", null)
  yield ()
  val emplHomeView = for    // ------ Employee home View
    _ <- addView("Empl-Home", BorderLayout(0, 10))

    _ <- addPanel("Empl-Title-Panel", FlowLayout(), "Empl-Home", BorderLayout.NORTH)
    _ <- addLabel("Empl-Title", "Benvenuto <DIPENDENTE>", "Empl-Title-Panel", FlowLayout.CENTER)

    _ <- addPanel("Empl-Panel-Outer", FlowLayout(), "Empl-Home", BorderLayout.SOUTH)
    _ <- addPanel("Empl-Panel", GridLayout(2, 1, 0, 10), "Empl-Panel-Outer", FlowLayout.CENTER)
    _ <- addButton("Empl-Home-Appointments", "Gestione appuntamenti", "Empl-Panel", null)
    _ <- addButton("Empl-Home-Logout", "Esci", "Empl-Panel", null)
  yield ()
  val emplAppointmentsView = for    // ------ Employee appointments View
    _ <- addView("Empl-Appointments", BorderLayout(0, 10))
    _ <- addButton("Empl-Appts-Back", "<", "Empl-Appointments", BorderLayout.WEST)

    _ <- addButton("Empl-Appts-Back", "<", "Empl-Appointments", BorderLayout.WEST)

    _ <- addPanel("Empl-Appts-Panel-Outer", FlowLayout(), "Empl-Appointments", BorderLayout.CENTER)
    _ <- addPanel("Empl-Appts-Panel", GridLayout(2, 1), "Empl-Appts-Panel-Outer", FlowLayout.CENTER)
    _ <- addLabel("Empl-Appts-List-Label", "Lista appuntamenti", "Empl-Appts-Panel", FlowLayout.CENTER)
    _ <- addList("Empl-Appts-List", Array("uno", "due"), "Empl-Appts-Panel", null)
  yield ()
  val displayView = for
    _ <- showView("Login")
    _ <- show()
    events <- eventStream()
  yield events

  val windowEventsHandling = for
    _ <- loginView
    _ <- userHomeView
    _ <- userAccountView
    _ <- userAppointmentsView
    _ <- userLoansView
    _ <- emplHomeView
    _ <- emplAppointmentsView
    e <- displayView
    _ <- seqN:
      e.map:
        case "Client-Login-Button" => showView("User-Home")
        case "Empl-Login-Button" => showView("Empl-Home")
        case "User-Home-Logout" => showView("Login")
        case "User-Home-Account" => showView("User-Account")
        case "User-Home-New-Account" => showView("User-Account")
        case "User-Home-Appointments" => showView("User-Appointments")
        case "User-Home-Sim-Loan" => showView("Loan-Simulator")
        case "Empl-Home-Appointments" => showView("Empl-Appointments")
        case "Empl-Home-Logout" => showView("Login")
//        case "Empl-Login-Button" =>
//          for
//            t <- getInputText("Username-Input")
//          yield println(s"Text: '${t}'")
        case "User-Account-Back" => showView("User-Home")
        case "User-Appts-Back" => showView("User-Home")
        case "Loan-Sim-Back" => showView("User-Home")
        case "Empl-Appts-Back" => showView("Empl-Home")
        case Frame.CLOSED => exec(sys.exit())
        case v => exec(println(s"No action: ${v}"))
  yield ()

  windowEventsHandling.run(initialWindow)