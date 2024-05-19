package scalabank.entities

import scalabank.entities.Manager.ManagerPosition
import scala.annotation.tailrec

/**
 * Represents a manager within the organization.
 */
trait Manager extends AbstractStaffMember[ManagerPosition]:
  /**
   * Retrieves the projects managed by the manager.
   *
   * @return An iterable collection of projects managed by the manager.
   */
  def projects: Iterable[Project]

  /**
   * Adds a new project to the manager.
   *
   * @param project The project to add.
   */
  def addProject(project: Project): Unit

  /**
   * Removes a project from the manager.
   *
   * @param project The project to remove.
   */
  def removeProject(project: Project): Unit


object Manager:
  /**
   * Enumeration representing different types of manager positions along with their associated salaries.
   */
  enum ManagerPosition(val salary: Double) extends StaffPosition:
    case TeamLead extends ManagerPosition(2000)
    case DepartmentHead extends ManagerPosition(3000)

  /**
   * Factory method to create a new manager.
   *
   * @param name       The manager name.
   * @param surname    The manager surname.
   * @param birthYear  The manager birth year.
   * @param position   The manager position.
   * @param hiringYear The manager hired year.
   * @param projects   The initial projects managed by the manager.
   * @return A new manager instance.
   */
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

    override def addProject(project: Project): Unit =
      currentProjects = currentProjects :+ project

    override def removeProject(project: Project): Unit =
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