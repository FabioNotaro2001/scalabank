package scalabank.gui

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import SwingFunctionalFacade.*
import WindowStateImpl.*

import java.awt.FlowLayout

@RunWith(classOf[JUnitRunner])
class GUITest extends AnyFlatSpec with BeforeAndAfterEach:
  val viewName = "View"
  val componentName = "Component"
  "Two views" should "not have the same name" in:
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addView(viewName, FlowLayout())
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "Two panels" should "not have the same name" in :
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addPanel(componentName, FlowLayout(), viewName, FlowLayout.LEFT)
      _ <- addPanel(componentName, FlowLayout(), viewName, FlowLayout.TRAILING)
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "Two buttons" should "not have the same name" in:
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addButton(componentName, "", viewName, FlowLayout.LEFT)
      _ <- addButton(componentName, "", viewName, FlowLayout.TRAILING)
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "Two inputs" should "not have the same name" in :
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addInput(componentName, 1, viewName, FlowLayout.LEFT)
      _ <- addInput(componentName, 1, viewName, FlowLayout.TRAILING)
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "Two labels" should "not have the same name" in :
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addLabel(componentName, "", viewName, FlowLayout.LEFT)
      _ <- addLabel(componentName, "", viewName, FlowLayout.TRAILING)
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "Two comboboxes" should "not have the same name" in :
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addComboBox(componentName, Array(), viewName, FlowLayout.LEFT)
      _ <- addComboBox(componentName, Array(), viewName, FlowLayout.TRAILING)
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "Two lists" should "not have the same name" in :
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addList(componentName, Array(), viewName, FlowLayout.LEFT)
      _ <- addList(componentName, Array(), viewName, FlowLayout.TRAILING)
    yield ()
    assertThrows[IllegalArgumentException]:
      windowCreation.run(createFrame())

  "A component" should "be added to an existing container" in:
      val viewName2 = "NonExistentView"
      val windowCreation = for
        _ <- addView(viewName, FlowLayout())
        _ <- addButton("", "", viewName2, FlowLayout.LEFT)
      yield ()
      assertThrows[IllegalArgumentException]:
        windowCreation.run(createFrame())

  "Two different types of components" can "have the same name" in:
    val viewName = "View"
    val componentName = "Component"
    val windowCreation = for
      _ <- addView(viewName, FlowLayout())
      _ <- addButton(componentName, "", viewName, FlowLayout.LEFT)
      _ <- addInput(componentName, 1, viewName, FlowLayout.LEFT)
    yield ()
    windowCreation.run(createFrame())
