package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.entities.Employee.EmployeePosition

import java.time.Year

@RunWith(classOf[JUnitRunner])
class EmployeeTest extends AnyFunSuite:
  test("An Employee should be correctly initialized and age calculated"):
    val employee = Employee("John", "Doe", 1990, EmployeePosition.FinancialAnalyst, 2000)
    employee.name shouldBe "John"
    employee.surname shouldBe "Doe"
    employee.birthYear shouldBe 1990
    employee.isAdult shouldBe true

  test("An Employee salary calculated"):
    val employee = Employee("John", "Doe", 1990, Employee.EmployeePosition.FinancialAnalyst, 2000)
    employee.salary shouldBe 1500.0

  test("Employee should calculate annual salary correctly"):
    val cashier = Employee("John", "Doe", 1990, Employee.EmployeePosition.Cashier, 2000)
    val financialAnalyst = Employee("Jane", "Smith", 1985, Employee.EmployeePosition.FinancialAnalyst, 2000)
    cashier.annualSalary shouldBe 12000
    financialAnalyst.annualSalary shouldBe 18000

  test("fullName should return the full name of the employee"):
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier, 2000)
    employee.fullName shouldBe "John Doe"

  test("annualNetSalary should calculate the net annual salary using the default tax rate"):
    val taxRateDefault: Double = 0.2
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier, 2000)
    val expectedNetSalary = employee.annualSalary * (1 - taxRateDefault)
    import Employee.taxRateDefault
    employee.annualNetSalary shouldBe expectedNetSalary

  test("annualNetSalary should calculate the net annual salary using not the default tax rate"):
    val taxRate: Double = 0.3
    val employee = Employee("John", "Doe", 1990, EmployeePosition.Cashier, 2000)
    val expectedNetSalary = employee.annualSalary * (1 - taxRate)
    employee.annualNetSalary(using taxRate) shouldBe expectedNetSalary

  test("Employee should calculate annual salary with standard bonus correctly"):
    import Employee.standardBonusRate
    val employee = Employee("John", "Doe", 1980, EmployeePosition.Cashier, 2010)
    employee.annualSalaryWithContextualBonus shouldBe employee.annualSalary * 1.1

  test("Employee should calculate annual salary with senior bonus correctly"):
    import Employee.seniorBonusRate
    val employee = Employee("Jane", "Doe", 1980, EmployeePosition.FinancialAnalyst, 2010)
    employee.annualSalaryWithContextualBonus shouldBe employee.annualSalary * 1.2

  test("Employee should be promoted correctly"):
    val employee = Employee("John", "Doe", 1980, EmployeePosition.Cashier, 2010)
    val promotedEmployee = employee.promote(EmployeePosition.FinancialAnalyst)
    promotedEmployee.position shouldBe EmployeePosition.FinancialAnalyst

  test("List of employees should calculate total annual salary correctly"):
    val employee1 = Employee("Alice", "Johnson", 1992, EmployeePosition.Cashier, 2000)
    val employee2 = Employee("Bob", "Williams", 1993, EmployeePosition.FinancialAnalyst, 2000)
    val employees = List(employee1, employee2)
    employees.totalAnnualSalary shouldBe (employee1.annualSalary + employee2.annualSalary)

  test("allJobCategories should return a list of annual salaries for all employees"):
    val employee1 = Employee("John", "Doe", 1990, EmployeePosition.Cashier, 2000)
    val employee2 = Employee("Jane", "Smith", 1985, EmployeePosition.FinancialAnalyst, 2000)
    val employees = List(employee1, employee2)
    val expectedSalaries = List(12000.0, 18000.0) // 1000 * 12, 1500 * 12
    employees.allEmployeesSalary shouldBe expectedSalaries

  test("yearsOfService should calculate the correct years of service"):
    val employee = Employee("John", "Doe", 1990, Employee.EmployeePosition.Cashier, 2000)
    employee.yearsOfService shouldBe (Year.now.getValue - employee.hiringYear)

  test("promote should change the employee's position"):
    val employee = Employee("John", "Doe", 1990, Employee.EmployeePosition.Cashier, 2000)
    val promotedEmployee = employee.promote(Employee.EmployeePosition.FinancialAnalyst)
    promotedEmployee.position shouldBe Employee.EmployeePosition.FinancialAnalyst