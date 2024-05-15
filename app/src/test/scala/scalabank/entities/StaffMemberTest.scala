package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.entities.Employee.Position

@RunWith(classOf[JUnitRunner])
class StaffMemberTest extends AnyFunSuite:
  test("An Employee should be correctly initialized and age calculated"):
    val employee = Employee("John", "Doe", 1990, Position.FinancialAnalyst)
    employee.name shouldBe "John"
    employee.surname shouldBe "Doe"
    employee.birthYear shouldBe 1990
    employee.isAdult shouldBe true
