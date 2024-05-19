package scalabank.entities

trait Project:
  def name: String
  def budget: Double
  def team: List[Employee]
  def addTeamMember(employee: Employee): Unit
  def removeTeamMember(employee: Employee): Unit

object Project:
  def apply(name: String, budget: Double, team: List[Employee]): Project = ProjectImpl(name, budget, team)

  private case class ProjectImpl(name: String, budget: Double, var team: List[Employee]) extends Project:
    def addTeamMember(employee: Employee): Unit =
      team = team :+ employee

    def removeTeamMember(employee: Employee): Unit =
      team = team.filterNot(_ == employee)