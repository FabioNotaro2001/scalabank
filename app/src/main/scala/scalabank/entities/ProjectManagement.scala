package scalabank.entities

trait ProjectManagement:
  def currentProjects: List[String]
  def addProject(project: String): Unit
  def removeProject(project: String): Unit