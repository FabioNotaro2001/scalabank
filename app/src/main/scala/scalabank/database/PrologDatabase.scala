package scalabank.database

import alice.tuprolog.{Prolog, Theory, SolveInfo}
import scala.io.Source

class PrologDatabase:
  private val engine = new Prolog()

  private def loadTheory(file: String): Theory =
    val source = Source.fromFile(file)
    val theory = new Theory(source.mkString)
    source.close()
    theory

  engine.addTheory(loadTheory("src/main/resources/persons.pl"))
  engine.addTheory(loadTheory("src/main/resources/employees.pl"))
  engine.addTheory(loadTheory("src/main/resources/managers.pl"))
  engine.addTheory(loadTheory("src/main/resources/projects.pl"))

  private def queryBase(query: String): Option[SolveInfo] =
    val solution = engine.solve(query)
    if (solution.isSuccess) Some(solution) else None

  def queryPerson(name: String): Option[SolveInfo] =
    val query = s"person { name '$name', age Age, nationality Nationality, married }."
    queryBase(query)

  def queryEmployee(name: String): Option[SolveInfo] =
    val query = s"employee { name '$name', age Age, nationality Nationality, married, position Position, hiringYear HiringYear, salary Salary }."
    queryBase(query)

  def queryManager(name: String): Option[SolveInfo] =
    val query = s"manager { name '$name', age Age, nationality Nationality, married, position Position, hiringYear HiringYear, salary Salary, projects Projects }."
    queryBase(query)

  def queryProject(name: String): Option[SolveInfo] =
    val query = s"project { name $name, budget Budget, team Team }."
    queryBase(query)

