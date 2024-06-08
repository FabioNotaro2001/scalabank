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
  def addComboBox(name: String, options: Array[String], panel: String, constraints: AnyRef): State[Window, Unit]
  def getComboBoxSelection(name: String): State[Window, String]
  def show(): State[Window, Unit]
  def exec(cmd: =>Unit): State[Window, Unit]
  def eventStream(): State[Window, LazyList[(String, String)]]

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
  def addInput(name: String, columns: Int, panel: String, constraints: Any): State[Window, Unit] =
    State(w => (w.addInput(name, columns, panel, constraints), {}))
  def getInputText(name: String): State[Window, String] =
    State(w => (w, w.getInputText(name)))
  def addComboBox(name: String, options: Array[String], panel: String, constraints: AnyRef): State[Window, Unit] =
    State(w => (w.addComboBox(name, options, panel, constraints), {}))
  def getComboBoxSelection(name: String): State[Window, String] =
    State(w => (w, w.getComboBoxSelection(name)))
  def changeLabel(name: String, text: String): State[Window, Unit] =
    State(w => (w.changeLabel(name, text), {}))
  def show(): State[Window, Unit] =
    State(w => (w.show, {}))
  def exec(cmd: =>Unit): State[Window, Unit] =
    State(w => (w, cmd))  
  def eventStream(): State[Window, LazyList[(String, String)]] =
    State(w => (w, LazyList.generate(w.events)))

@main def testView(): Unit =
  import WindowStateImpl.*
  import Monads.*, Monad.*
  val windowCreation = for
    _ <- setSize(600, 300)
    _ <- setMinSize(200, 100)
    // ------ Login View
    _ <- addView("Login", BorderLayout(0, 40))
    _ <- addPanel("Login-Title-Panel", FlowLayout(), "Login", BorderLayout.NORTH)
    _ <- addLabel("Login-Title", "Scalabank", "Login-Title-Panel", FlowLayout.CENTER)

    _ <- addPanel("Login-Inputs-Panel", GridLayout(2, 2), "Login", BorderLayout.CENTER)
    - <- addPanel("Username-Label-Panel", FlowLayout(), "Login-Inputs-Panel", null)
    _ <- addLabel("Username-Label", "Username:", "Username-Label-Panel", FlowLayout.CENTER)
    _ <- addInput("Username-Input", 20, "Login-Inputs-Panel", null)
    - <- addPanel("Password-Label-Panel", FlowLayout(), "Login-Inputs-Panel", null)
    _ <- addLabel("Password-Label", "Password:", "Password-Label-Panel", FlowLayout.CENTER)
    _ <- addInput("Password-Input", 20, "Login-Inputs-Panel", null)

    _ <- addPanel("Login-Button-Panel", FlowLayout(), "Login", BorderLayout.SOUTH)
    _ <- addButton("Client-Login-Button", "Login come cliente", "Login-Button-Panel", FlowLayout.LEFT)
    _ <- addButton("Empl-Login-Button", "Login come dipendente", "Login-Button-Panel", FlowLayout.TRAILING)
    // ------ User home View
    _ <- addView("User-Home", BorderLayout(0, 40))
    _ <- addPanel("User-Title-Panel", FlowLayout(), "User-Home", BorderLayout.NORTH)
    _ <- addLabel("User-Title", "TITLE HERE", "User-Title-Panel", FlowLayout.CENTER)
    _ <- addPanel("User-Home-Panel", GridLayout(3, 2), "User-Home", BorderLayout.CENTER)
    _ <- addButton("User-Home-View-Account", "Vedi conto", "User-Home-Panel", FlowLayout.TRAILING)
    _ <- addButton("User-Home-Appointments", "Gestione appuntamenti", "User-Home-Panel", FlowLayout.TRAILING)
    _ <- addButton("User-Home-New-Account", "Apri conto", "User-Home-Panel", FlowLayout.TRAILING)
    _ <- addButton("User-Home-Sim-Loan", "Simulatore mutuo", "User-Home-Panel", FlowLayout.TRAILING)
    _ <- addButton("User-Home-Logout", "Esci", "User-Home-Panel", FlowLayout.TRAILING)
    // ------ User account View
    _ <- addView("User-Account", BorderLayout(0, 40))
    // ------ User appointments View
    _ <- addView("User-Appointments", BorderLayout(0, 40))
    // ------ User loans View
    _ <- addView("User-Loans", BorderLayout(0, 40))
    // ------ Employee home View
    _ <- addView("Empl-Home", BorderLayout(0, 40))
    // ------ Employee appointments View
    _ <- addView("Empl-Appointments", BorderLayout(0, 40))

    _ <- showView("Login")
    _ <- show()
    events <- eventStream()
  yield events

  val windowEventsHandling = for
    e <- windowCreation
    _ <- seqN:
      e.map:
        case ("Client-Login-Button", _) => showView("User-Home")
        case ("Empl-Login-Button", _) =>
          for
            t <- getInputText("Username-Input")
          yield println(s"Text: '${t}'")
        case (Frame.CLOSED, _) => exec(sys.exit())
        case v => exec(println(s"No action: ${v}"))
  yield ()

  windowEventsHandling.run(initialWindow)