package scalabank.testLogger

import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.*
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

/*@RunWith(classOf[JUnitRunner])
class TestLogger extends AnyFlatSpec with BeforeAndAfterEach:

  "The logger" should "print a string" in:
    Logger.log("hello")

  "The logger" should "not print if used while it is not enabled" in:
    Logger.disable()
    Logger.log("hello")
    Logger.enable()

  // TODO: Aggiungere test di log con employee, manager ecc*/