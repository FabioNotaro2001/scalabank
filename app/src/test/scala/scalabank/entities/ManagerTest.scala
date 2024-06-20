package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.Logger

import java.time.Year

@RunWith(classOf[JUnitRunner])
class ManagerTest extends AnyFunSuite:
  test("Manager should add a project correctly"):
    val project = Project("Project A", 50000.0, List())
    val manager = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List())
    manager.addProject(project)
    manager.projects should contain (project)

  test("Manager should remove a project correctly"):
    val project = Project("Project A", 50000.0, List())
    val manager = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(project))
    manager.removeProject(project)
    manager.projects should not contain project

  test("Manager should calculate the correct project budget"):
    val projectA = Project("Project A", 50000.0, List())
    val projectB = Project("Project B", 75000.0, List())
    val manager = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(projectA, projectB))
    manager.projectBudget("Project A") shouldBe 50000.0
    manager.projectBudget("Project B") shouldBe 75000.0
    manager.projectBudget("NonExistentProject") shouldBe 0.0

  test("Manager should calculate the total project budgets correctly"):
    val projectA = Project("Project A", 50000.0, List())
    val projectB = Project("Project B", 75000.0, List())
    val manager = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(projectA, projectB))
    manager.totalProjectBudgets shouldBe 125000.0

  test("List of Managers should calculate the total projects managed correctly"):
    val projectA = Project("Project A", 50000.0, List())
    val projectB = Project("Project B", 75000.0, List())
    val projectC = Project("Project C", 100000.0, List())
    val manager1 = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List(projectA))
    val manager2 = Manager("JHNSMT22B705Y","Jane", "Smith", 1985, Manager.ManagerPosition.DepartmentHead, 2015, List(projectB, projectC))
    val managers = List(manager1, manager2)
    managers.totalProjectsManaged shouldBe 3

  test("Project should add a team member correctly"):
    val employee = Employee("JHNLCAE22B705Y", "Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val project = Project("Project A", 50000.0, List())
    project.addTeamMember(employee)
    project.team should contain (employee)

  test("Project should remove a team member correctly"):
    val employee = Employee("JHNLCAE22B705Y", "Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val project = Project("Project A", 50000.0, List(employee))
    project.removeTeamMember(employee)
    project.team should not contain (employee)

  test("Project should handle team member updates correctly"):
    val employee1 = Employee("JHNLCAE22B705Y", "Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2000)
    val employee2 = Employee("WLLBBOE22B705Y", "Bob", "Williams", 1993, Employee.EmployeePosition.FinancialAnalyst, 2000)
    val project = Project("Project A", 50000.0, List(employee1))
    project.addTeamMember(employee2)
    project.team should contain (employee1)
    project.team should contain (employee2)
    project.removeTeamMember(employee1)
    project.team should not contain (employee1)
    project.team should contain (employee2)

  test("Manager should calculate years of service correctly"):
    val manager = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List())
    val currentYear = Year.now.getValue
    manager.yearsOfService shouldBe (currentYear - 2010)

  test("Manager should calculate annual net salary correctly"):
    given taxRate: Double = 0.25
    val manager = Manager("JHNDOE22B705Y", "John", "Doe", 1980, Manager.ManagerPosition.TeamLead, 2010, List())
    val expectedNetSalary = manager.annualSalary * (1 - taxRate)
    manager.annualNetSalary shouldBe expectedNetSalary
