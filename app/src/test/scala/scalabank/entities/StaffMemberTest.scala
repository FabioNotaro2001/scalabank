package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.entities.Employee.EmployeePosition

@RunWith(classOf[JUnitRunner])
class StaffMemberTest extends AnyFunSuite:
  test("An Employee should be correctly initialized and age calculated"):
    val employee = Employee("John", "Doe", 1990, EmployeePosition.FinancialAnalyst)
    employee.name shouldBe "John"
    employee.surname shouldBe "Doe"
    employee.birthYear shouldBe 1990
    employee.isAdult shouldBe true

  test("An Employee salary calculated"):
    val employee = Employee("John", "Doe", 1990, Employee.EmployeePosition.FinancialAnalyst)
    employee.salary shouldBe 1500.0

  test("Employee should calculate annual salary correctly"):
    val cashier = Employee("John", "Doe", 1990, Employee.EmployeePosition.Cashier)
    val financialAnalyst = Employee("Jane", "Smith", 1985, Employee.EmployeePosition.FinancialAnalyst)
    cashier.annualSalary shouldBe 12000
    financialAnalyst.annualSalary shouldBe 18000

  test("fullName should return the full name of the employee"):
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier)
    employee.fullName shouldBe "John Doe"

  test("annualSalaryWithBonus should return the annual salary with the bonus added"):
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier)
    val bonus = 500
    val expectedSalaryWithBonus = employee.annualSalary + bonus
    employee.annualSalaryWithBonus(bonus) shouldBe expectedSalaryWithBonus

  test("annualNetSalary should calculate the net annual salary using the default tax rate"):
    val taxRateDefault: Double = 0.2
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier)
    val expectedNetSalary = employee.annualSalary * (1 - taxRateDefault)
    import Employee.given
    employee.annualNetSalary shouldBe expectedNetSalary

  test("annualNetSalary should calculate the net annual salary using not the default tax rate"):
    val taxRate: Double = 0.3
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier)
    val expectedNetSalary = employee.annualSalary * (1 - taxRate)
    employee.annualNetSalary(using taxRate) shouldBe expectedNetSalary

  test("allJobCategories should return a list of annual salaries for all employees"):
    val employee1 = Employee("John", "Doe", 1990, EmployeePosition.Cashier)
    val employee2 = Employee("Jane", "Smith", 1985, EmployeePosition.FinancialAnalyst)

    val employees = List(employee1, employee2)
    val expectedSalaries = List(12000.0, 18000.0) // 1000 * 12, 1500 * 12
    employees.allEmployeesSalary shouldBe expectedSalaries