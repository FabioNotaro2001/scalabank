package scalabank.entities

import scalabank.entities.Person

trait Customer extends Person

object Customer:
  def apply(name: String, surname: String, birthYear: Int): Customer = CustomerImpl(name, surname, birthYear)

case class CustomerImpl(_name: String,
                        _surname: String,
                        _birthYear: Int) extends Customer:
  private val person = Person(_name, _surname, _birthYear)
  export person.*