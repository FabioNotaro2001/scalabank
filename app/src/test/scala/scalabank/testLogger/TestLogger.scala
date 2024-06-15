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
    assert(logger.isEnabledNow)

  "The method disable" should "disable logger" in:
    logger.disable()
    assert(!logger.isEnabledNow)

  "The method enable" should "enable the logger" in:
    logger.disable()
    logger.enable()
    assert(logger.isEnabledNow)

  "The logger" should "print correctly to console" in:
    logger.log("This string should be printed")

  "The logger" should "not print to console when disabled" in:
    logger.disable()
    logger.log("This string should not be printed")
    logger.enable()

  "A client entity" should "be logged with its creation" in:
    val employee = Employee("Mario", "Rossi", 1960, EmployeePosition.FinancialAnalyst, 1500)