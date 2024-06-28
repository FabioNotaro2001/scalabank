package scalabank.entities

import scalabank.entities.Manager.ManagerPosition
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

import scala.annotation.tailrec

/**
 * Represents a manager within the organization.
 */
trait Manager extends StaffMember[ManagerPosition]:
  /**
   * Retrieves the projects managed by the manager.
   *
   * @return An iterable collection of projects managed by the manager.
   */
  def projects: List[Project]

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

trait ManagerComponent:
  loggerDependency: LoggerDependency =>
  case class ManagerImpl(person: Person,
                         override val position: ManagerPosition,
                         override val hiringYear: Int,
                         private var currentProjects: List[Project]) extends Manager:
    export person.*

    override def projects: List[Project] = currentProjects

    override def addProject(project: Project): Unit =
      currentProjects = currentProjects :+ project

    override def removeProject(project: Project): Unit =
      currentProjects = currentProjects.filterNot(_ == project)

object Manager extends LoggerDependency with ManagerComponent:
  override val logger: Logger = LoggerImpl()

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
  def apply(cf: String,
            name: String,
            surname: String,
            birthYear: Int,
            position: ManagerPosition,
            hiringYear: Int,
            projects: List[Project]): Manager =
    val manager = ManagerImpl(Person(cf, name, surname, birthYear), position, hiringYear, projects)
    logger.log(logger.getPrefixFormatter.getCreationPrefix + manager)
    manager

  extension (manager: Manager)
    /**
     * Retrieves the budget of a specific project managed by the manager.
     *
     * @param projectName The name of the project.
     * @return The budget of the specified project, or 0.0 if the project is not found.
     */
    def projectBudget(projectName: String): Double =
      manager.projects.find(_.name == projectName).map(_.budget).getOrElse(0.0)

    /**
     * Calculates the total budget of all projects managed by a manager.
     *
     * @return The total budget of all projects.
     */
    def totalProjectBudgets: Double = manager.projects.map(_.budget).sum

  extension (managers: List[Manager])
    /**
     * Calculates the total number of projects managed by a list of managers.
     *
     * @return The total number of projects.
     */
    def totalProjectsManaged: Int =
      @tailrec
      def countProjects(managers: List[Manager], acc: Int = 0): Int =
        managers match
          case Nil => acc
          case head :: tail => countProjects(tail, acc + head.projects.size)
      countProjects(managers)