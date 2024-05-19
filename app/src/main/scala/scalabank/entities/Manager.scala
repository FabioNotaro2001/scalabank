package scalabank.entities

import scalabank.entities.Manager.ManagerPosition
import scala.annotation.tailrec

trait Manager extends StaffMember[ManagerPosition]:
  def projects: Iterable[Project]
  def addProject(project: Project): Unit
  def removeProject(project: Project): Unit


object Manager:
  enum ManagerPosition(val salary: Double) extends StaffPosition:
    case TeamLead extends ManagerPosition(2000)
    case DepartmentHead extends ManagerPosition(3000)

  def apply(name: String,
            surname: String,
            birthYear: Int,
            position: ManagerPosition,
            hiringYear: Int,
            projects: List[Project]): Manager =
    ManagerImpl(Person(name, surname, birthYear), position, hiringYear, projects)

  private case class ManagerImpl(person: Person,
                                 override val position: ManagerPosition,
                                 override val hiringYear: Int,
                                 var currentProjects: List[Project]) extends Manager:
    export person.*

    override def projects: Iterable[Project] = currentProjects.view

    def addProject(project: Project): Unit =
      currentProjects = currentProjects :+ project

    def removeProject(project: Project): Unit =
      currentProjects = currentProjects.filterNot(_ == project)

  extension (manager: Manager)
    def projectBudget(projectName: String): Double =
      manager.projects.find(_.name == projectName).map(_.budget).getOrElse(0.0)

    def totalProjectBudgets: Double = manager.projects.map(_.budget).sum

  extension (managers: List[Manager])
    def totalProjectsManaged: Int =
      @tailrec
      def countProjects(managers: List[Manager], acc: Int = 0): Int =
        managers match
          case Nil => acc
          case head :: tail => countProjects(tail, acc + head.projects.size)
      countProjects(managers)