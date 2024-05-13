package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.shouldBe

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


