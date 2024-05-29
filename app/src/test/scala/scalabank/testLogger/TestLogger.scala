package scalabank.testLogger

import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.*
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestLogger extends AnyFlatSpec with BeforeAndAfterEach:

  "The logger" should "print a string" in:
    Logger.log("hello")

  "The logger" should "throw an exception if used while it is not enabled" in:
    Logger.disable()
    an [IllegalArgumentException] should be thrownBy Logger.log("hello")
    Logger.enable()