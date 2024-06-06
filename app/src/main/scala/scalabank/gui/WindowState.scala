package scalabank.gui

import Monads.*
import States.*
import State.*

import java.util.function.Supplier

trait WindowState:
  type Window
  def initialWindow: Window
  def setSize(width: Int, height: Int): State[Window, Unit]
  def addButton(text: String, name: String): State[Window, Unit]
  def addLabel(text: String, name: String): State[Window, Unit]
  def toLabel(text: String, name: String): State[Window, Unit]
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
  def addButton(text: String, name: String): State[Window, Unit] =
    State(w => ((w.addButton(text, name)), {}))
  def addLabel(text: String, name: String): State[Window, Unit] =
    State(w => ((w.addLabel(text, name)), {}))
  def toLabel(text: String, name: String): State[Window, Unit] =
    State(w => ((w.showToLabel(text, name)), {}))
  def show(): State[Window, Unit] =
    State(w => (w.show, {}))
  def exec(cmd: =>Unit): State[Window, Unit] =
    State(w => (w, cmd))  
  def eventStream(): State[Window, LazyList[String]] =
    State(w => (w, LazyList.generate(w.events)))
