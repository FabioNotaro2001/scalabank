package scalabank.entities

import java.util.Calendar

trait Person:
  def name: String
  def surname: String
  def birthYear: Int
  def isAdult: Boolean


object Person:
  def apply(name: String, surname: String, birthYear: Int): Person = PersonImpl(name, surname, birthYear)

  private class PersonImpl(override val name: String, override val surname: String, override val birthYear: Int) extends Person:
    private val age: Int = Calendar.getInstance().get(Calendar.YEAR) - birthYear

    override def isAdult: Boolean = age >= 18

  extension (person: Person)
    def isYoungerThan(other: Person): Boolean = person.birthYear > other.birthYear
