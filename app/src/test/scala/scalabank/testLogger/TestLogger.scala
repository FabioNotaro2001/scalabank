package scalabank.testLogger

import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.*
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestLogger extends AnyFlatSpec with BeforeAndAfterEach:
  override def beforeEach(): Unit = Logger.reset()

  "The logger" should "print a string" in:
    Logger.log("hello")

  "The logger" should "save a string" in :
    Logger.save("ciao")

  "The logger" should "be empty at the beginning" in:
    Logger.getSize() shouldBe 0

  "The logger size" should "grow correctly" in:
    Logger.save("one")
    Logger.save("two")
    Logger.getSize() shouldBe 2

  "The logger" should "print all saved strings" in:
    Logger.save("one")
    Logger.save("two")
    Logger.logAll()
    Logger.getSize() shouldBe 0