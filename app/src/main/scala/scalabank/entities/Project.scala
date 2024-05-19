package scalabank.entities

/**
 * A trait representing a project.
 * Projects have a name, a budget, and a team of employees.
 */
trait Project:
  /**
   * The project name.
   *
   * @return The name of the project.
   */
  def name: String

  /**
   * The project budget.
   *
   * @return The budget of the project.
   */
  def budget: Double

  /**
   * Team working on the project.
   *
   * @return The team working on the project as a List of Employees.
   */
  def team: List[Employee]

  /**
   * Adds a new team member to the project.
   *
   * @param employee The employee to add to the project's team.
   */
  def addTeamMember(employee: Employee): Unit

  /**
   * Removes a team member from the project.
   *
   * @param employee The employee to remove from the project's team.
   */
  def removeTeamMember(employee: Employee): Unit

object Project:
  def apply(name: String, budget: Double, team: List[Employee]): Project = ProjectImpl(name, budget, team)

  private case class ProjectImpl(name: String, budget: Double, var team: List[Employee]) extends Project:
    def addTeamMember(employee: Employee): Unit =
      team = team :+ employee

    def removeTeamMember(employee: Employee): Unit =
      team = team.filterNot(_ == employee)