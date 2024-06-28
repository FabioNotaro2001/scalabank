package scalabank.entities

/**
 * A project.
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

/**
 * Factory object for creating Project instances.
 */
object Project:

  /**
   * Creates a new Project instance.
   *
   * @param name   the name of the project
   * @param budget the budget allocated for the project
   * @param team   the list of employees assigned to the project
   * @return a new Project instance
   */
  def apply(name: String, budget: Double, team: List[Employee]): Project = ProjectImpl(name, budget, team)

  private case class ProjectImpl(name: String, budget: Double, var team: List[Employee]) extends Project:
    override def addTeamMember(employee: Employee): Unit =
      team = team :+ employee

    override def removeTeamMember(employee: Employee): Unit =
      team = team.filterNot(_ == employee)