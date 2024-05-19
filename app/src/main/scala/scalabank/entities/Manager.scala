package scalabank.entities

import scalabank.entities.Manager.ManagerPosition
import scala.annotation.tailrec

trait Manager extends StaffMember[ManagerPosition] with ProjectManagement:
  def team: List[Employee]
  def addTeamMember(employee: Employee): Unit
  def removeTeamMember(employee: Employee): Unit

object Manager:
  enum ManagerPosition(val salary: Double) extends StaffPosition:
    case TeamLead extends ManagerPosition(2000)
    case DepartmentHead extends ManagerPosition(3000)

  def apply(name: String, surname: String, birthYear: Int, position: ManagerPosition, hiringYear: Int, team: List[Employee], projects: List[String]): Manager =
    ManagerImpl(Person(name, surname, birthYear), position, hiringYear, team, projects)

  private case class ManagerImpl(person: Person, override val position: ManagerPosition, override val hiringYear: Int, var team: List[Employee], var currentProjects: List[String]) extends Manager:
    export person.*

    def addTeamMember(employee: Employee): Unit =
      team = team :+ employee

    def removeTeamMember(employee: Employee): Unit =
      team = team.filterNot(_ == employee)

    def addProject(project: String): Unit =
      currentProjects = currentProjects :+ project

    def removeProject(project: String): Unit =
      currentProjects = currentProjects.filterNot(_ == project)

  extension (manager: Manager)
    def projectBudget(projectBudgets: Map[String, Double]): Double =
      (for project <- manager.currentProjects yield projectBudgets.getOrElse(project, 0.0)).sum

    def teamAnnualSalaries: Double = manager.team.map(_.annualSalary).sum

  extension (managers: List[Manager])
    def totalEmployeesUnderManagement: Int =
      @tailrec
      def countEmployees(managers: List[Manager], acc: Int = 0): Int =
        managers match
          case Nil => acc
          case head :: tail => countEmployees(tail, acc + head.team.size)
      countEmployees(managers)