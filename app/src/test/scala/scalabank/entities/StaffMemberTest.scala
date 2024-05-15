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

  test("An Employee salary calculated"):
    val employee = Employee("John", "Doe", 1990, Employee.Position.FinancialAnalyst)
    employee.salary shouldBe 1500.0

  test("Employee should calculate annual salary correctly"):
    val cashier = Employee("John", "Doe", 1990, Employee.Position.Cashier)
    val financialAnalyst = Employee("Jane", "Smith", 1985, Employee.Position.FinancialAnalyst)
    cashier.annualSalary shouldBe 12000
    financialAnalyst.annualSalary shouldBe 18000