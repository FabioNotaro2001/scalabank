package scalabank.database

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.Person

@RunWith(classOf[JUnitRunner])
class PersonTest extends AnyFlatSpec:
  //val db = Database("jdbc:h2:./database/test")
  val db: Database = Database("jdbc:h2:mem:test4;DB_CLOSE_DELAY=-1")
  val person: Person = Person("ABCDEF12G34H567I", "Mario", "Rossi", 1990)

  "PersonDatabase" should "insert and find a person" in :
    db.personTable.insert(person)
    val retrievedPerson = db.personTable.findById("ABCDEF12G34H567I")
    retrievedPerson should be(defined)
    retrievedPerson.get shouldEqual person

  it should "update a person" in :
    val updatedPerson = Person("ABCDEF12G34H567I", "Luigi", "Bianchi", 1990)
    db.personTable.update(updatedPerson)
    val retrievedPerson = db.personTable.findById("ABCDEF12G34H567I")
    retrievedPerson should be(defined)
    retrievedPerson.get.name shouldEqual "Luigi"
    retrievedPerson.get.surname shouldEqual "Bianchi"

  it should "delete a person" in :
    db.personTable.delete("ABCDEF12G34H567I")
    val retrievedPerson = db.personTable.findById("ABCDEF12G34H567I")
    retrievedPerson should be(empty)

  it should "find all people" in :
    val person1 = Person("ABFGVF12G34H567I", "Mario", "Pastori", 1990)
    val person2 = Person("XYZ12345T67L890M", "Luigi", "Verdi", 1985)
    db.personTable.insert(person1)
    db.personTable.insert(person2)
    val allPeople = db.personTable.findAll()
    allPeople should contain allOf(person1, person2)




