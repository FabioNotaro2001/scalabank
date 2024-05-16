package scalabank.entities

import java.util.Calendar

trait Person:
  def name: String
  def surname: String
  def birthYear: Int
  def age: Int
  def isAdult: Boolean


object Person:
  def apply(name: String, surname: String, birthYear: Int): Person = PersonImpl(name, surname, birthYear)

  private case class PersonImpl( override val name: String,
                            override val surname: String,
                            birthY: Int) extends Person:

    override def birthYear: Int =
      require(birthY <= Calendar.getInstance().get(Calendar.YEAR))
      birthY

    override def age: Int = Calendar.getInstance().get(Calendar.YEAR) - birthYear

    override def isAdult: Boolean = age >= 18

  extension (person: Person)
    def isYoungerThan(other: Person): Boolean = person.birthYear > other.birthYear
    def ageDifference(other: Person): Int = Math.abs(person.birthYear - other.birthYear)
