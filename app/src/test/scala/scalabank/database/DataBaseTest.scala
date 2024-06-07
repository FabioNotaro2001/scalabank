package scalabank.database

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DataBaseTest extends AnyFlatSpec with BeforeAndAfterEach:
  val prologDB = new PrologDatabase()

  "The Prolog database" should "return the correct person details" in:
    prologDB.queryPerson("Rossi Marco") match
      case Some(solution) => solution.getSolution.toString should include("name 'Rossi Marco'")
      case None => fail("Person not found")

  it should "return the correct employee details" in:
    prologDB.queryEmployee("John Doe") match
      case Some(solution) => solution.getSolution.toString should include("name 'John Doe'")
      case None => fail("Employee not found")

  it should "return the correct manager details" in:
    prologDB.queryManager("Alice Johnson") match
      case Some(solution) => solution.getSolution.toString should include("name 'Alice Johnson'")
      case None => fail("Manager not found")

  it should "return the correct project details" in:
    prologDB.queryProject("project1") match
      case Some(solution) => solution.getSolution.toString should include("name project1")
      case None => fail("Project not found")


