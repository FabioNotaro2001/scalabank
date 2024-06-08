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
  def addView(name: String, layout: LayoutManager): State[Window, Unit]
  def showView(name: String): State[Window, Unit]
  def addPanel(name: String, layout: LayoutManager, view: String, constraints: Any): State[Window, Unit]
  def addButton(name: String, text: String, view: String, constraints: Any): State[Window, Unit]
  def addLabel(name: String, text: String, view: String, constraints: Any): State[Window, Unit]
  def changeLabel(name: String, text: String): State[Window, Unit]
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
  def changeLabel(name: String, text: String): State[Window, Unit] =
    State(w => (w.changeLabel(name, text), {}))
  def show(): State[Window, Unit] =
    State(w => (w.show, {}))
  def exec(cmd: =>Unit): State[Window, Unit] =
    State(w => (w, cmd))  
  def eventStream(): State[Window, LazyList[String]] =
    State(w => (w, LazyList.generate(w.events)))

@main def testView(): Unit =
  import WindowStateImpl.*
  import Monads.*, Monad.*
  val windowCreation = for
    _ <- setSize(600, 300)
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
    _ <- addButton("Employee-Login-Button", "Login come dipendente", "Login-Button-Panel", FlowLayout.TRAILING)

    _ <- showView("Login")
    _ <- show()
    events <- eventStream()
  yield events

  val windowEventsHandling = for
    e <- windowCreation
    _ <- seqN:
      e.map:
        case Frame.CLOSED => exec(sys.exit())
        case v => exec(println(s"No action: ${v}"))
  yield ()

  windowEventsHandling.run(initialWindow)