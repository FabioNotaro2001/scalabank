package scalabank.testLogger

import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.Employee
import scalabank.entities.Employee.EmployeePosition

@RunWith(classOf[JUnitRunner])
class TestLogger extends AnyFlatSpec:
  val logger: Logger = LoggerImpl()

  "The logger" should "be enabled by default" in:
    logger.isEnabledNow shouldBe true

  "The method disable" should "disable logger" in:
    logger.disable()
    logger.isEnabledNow shouldBe false

  "The method enable" should "enable the logger" in:
    logger.disable()
    logger.enable()
    logger.isEnabledNow shouldBe true

  "The logger" should "print correctly to console" in:
    logger.log("This string should be printed")

  "The logger" should "not print to console when disabled" in:
    logger.disable()
    logger.log("This string should not be printed")
    logger.enable()

  "A client entity" should "be logged with its creation" in:
    val employee = Employee("RSSMRA26D705Y", "Mario", "Rossi", 1960, EmployeePosition.FinancialAnalyst, 1500)