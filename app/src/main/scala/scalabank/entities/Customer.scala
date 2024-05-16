package scalabank.entities

import scalabank.entities.Person

trait Customer extends Person

trait YoungCustomer extends Customer

trait BaseCustomer extends Customer

object Customer:
  def apply(name: String, surname: String, birthYear: Int): Customer = Person(name, surname, birthYear) match
    case person if person.age < 35 => YoungCustomerImpl(name, surname, birthYear)
    case _ => BaseCustomerImpl(name, surname, birthYear)

case class YoungCustomerImpl(_name: String,
                             _surname: String,
                             _birthYear: Int) extends YoungCustomer:
  private val person = Person(_name, _surname, _birthYear)
  export person.*

case class BaseCustomerImpl(_name: String,
                             _surname: String,
                             _birthYear: Int) extends BaseCustomer:
  private val person = Person(_name, _surname, _birthYear)
  export person.*