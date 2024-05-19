package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*

@RunWith(classOf[JUnitRunner])
class ManagerTest extends AnyFunSuite:
  test("Manager should add a team member correctly"):
    val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val manager = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(), List())
    manager.addTeamMember(employee)
    manager.team should contain (employee)

  test("Manager should remove a team member correctly"):
    val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val manager = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(employee), List())
    manager.removeTeamMember(employee)
    manager.team should not contain (employee)

  test("Manager should add a project correctly"):
    val manager = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(), List())
    manager.addProject("Project A")
    manager.currentProjects should contain ("Project A")

  test("Manager should remove a project correctly"):
    val manager = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(), List("Project A"))
    manager.removeProject("Project A")
    manager.currentProjects should not contain "Project A"

  test("Manager should calculate the correct project budget"):
    val manager = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(), List("Project A", "Project B"))
    val projectBudgets = Map("Project A" -> 50000.0, "Project B" -> 75000.0)
    manager.projectBudget(projectBudgets) shouldBe 125000.0

  test("Manager should calculate the correct team annual salaries"):
    val employee1 = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val employee2 = Employee("Bob", "Williams", 1993, Employee.EmployeePosition.FinancialAnalyst, 2000)
    val manager = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(employee1, employee2), List())
    manager.teamAnnualSalaries shouldBe (employee1.annualSalary + employee2.annualSalary)

  test("List of Managers should calculate the total employees under management correctly"):
    val employee1 = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val employee2 = Employee("Bob", "Williams", 1993, Employee.EmployeePosition.FinancialAnalyst, 2000)
    val manager1 = Manager("John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(employee1), List())
    val manager2 = Manager("Jane", "Smith", 1985, Manager.ManagerPosition.DepartmentHead, 2015, List(employee2), List())
    val managers = List(manager1, manager2)
    managers.totalEmployeesUnderManagement shouldBe 2
