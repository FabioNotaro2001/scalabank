package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*


@RunWith(classOf[JUnitRunner])
class PersonTest extends AnyFunSuite:

  test("Person should be correctly initialized and age calculated"):
    val person = Person("John", "Doe", 1990)

    person.name shouldBe "John"
    person.surname shouldBe "Doe"
    person.birthYear shouldBe 1990
    person.isAdult shouldBe true

  test("Person should not be adult if under 18"):
    val person = Person("Alice", "Smith", 2020)
    person.isAdult shouldBe false

  test("isYoungerThan should return true if person is younger than the other person"):
    val john = Person("John", "Doe", 1990)
    val jane = Person("Jane", "Smith", 1985)
    john.isYoungerThan(jane) shouldBe true

  test("isYoungerThan should return false if person is older than the other person"):
    val john = Person("John", "Doe", 1990)
    val jane = Person("Jane", "Smith", 2000)
    john.isYoungerThan(jane) shouldBe false

  test("ageDifference should return the difference in birth years if person's birth year is greater than other's"):
    val john = Person("John", "Doe", 1990)
    val jane = Person("Jane", "Smith", 1985)
    john.ageDifference(jane) shouldBe 5

  test("birth year should be less than the current year"):
    a [ IllegalArgumentException ] should be thrownBy Person("John", "Doe", 2028)

  test("age should be calculated correctly"):
    val john = Person("John", "Doe", 1990)
    john.age shouldBe 34




