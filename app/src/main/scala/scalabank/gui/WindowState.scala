package scalabank.gui

import Monads.*
import States.*
import State.*
import scalabank.gui.SwingFunctionalFacade.Frame

import java.awt.{BorderLayout, FlowLayout, LayoutManager}
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
    State(w => ((w.setSize(width, height)), {}))

  def addView(name: String, layout: LayoutManager): State[Window, Unit] =
    State(w => ((w.addView(name, layout)), {}))
  def showView(name: String): State[Window, Unit] =
    State(w => ((w.showView(name)), {}))
  def addPanel(name: String, layout: LayoutManager, view: String, constraints: Any): State[Window, Unit] =
    State(w => ((w.addPanel(name, layout, view, constraints)), {}))
  def addButton(name: String, text: String, view: String, constraints: Any): State[Window, Unit] =
    State(w => ((w.addButton(name, text, view, constraints)), {}))
  def addLabel(name: String, text: String, view: String, constraints: Any): State[Window, Unit] =
    State(w => ((w.addLabel(name, text, view, constraints)), {}))
  def changeLabel(name: String, text: String): State[Window, Unit] =
    State(w => ((w.changeLabel(name, text)), {}))
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
    _ <- setSize(600, 500)
    _ <- addView("Login", BorderLayout())
    _ <- addLabel("LoginTitle", "Scalabank", "Login", BorderLayout.NORTH)
    _ <- addPanel("LoginCenterPanel", FlowLayout(), "Login", BorderLayout.CENTER)
    _ <- addButton("LoginButton", "Login come cliente", "LoginCenterPanel", FlowLayout.LEFT)
    _ <- addButton("LoginButton", "Login come dipendente", "LoginCenterPanel", FlowLayout.TRAILING)
    _ <- showView("Login")
    _ <- show()
    events <- eventStream()
  yield events

  val windowEventsHandling = for
    e <- windowCreation
    _ <- seqN(e.map:
      case Frame.CLOSED => exec(sys.exit()) // FIXME: NON VA
    )
  yield ()

  windowEventsHandling.run(initialWindow)