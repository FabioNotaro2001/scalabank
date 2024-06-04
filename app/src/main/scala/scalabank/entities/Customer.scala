package scalabank.entities

import scalabank.entities.Employee.logger
import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

trait Customer extends Person:
  def fidelity: Fidelity

trait YoungCustomer extends Customer

trait BaseCustomer extends Customer

trait CustomerComponent:
  loggerDependency: LoggerDependency =>
  case class YoungCustomerImpl(_name: String,
                               _surname: String,
                               _birthYear: Int) extends YoungCustomer:
    override def fidelity: Fidelity = Fidelity(0)

    private val person = Person(_name, _surname, _birthYear)
    export person.*

  case class BaseCustomerImpl(_name: String,
                              _surname: String,
                              _birthYear: Int) extends BaseCustomer:
    override def fidelity: Fidelity = Fidelity(0)

    private val person = Person(_name, _surname, _birthYear)
    export person.*

object Customer extends LoggerDependency with CustomerComponent:
  override val logger: Logger = LoggerImpl()
  def apply(name: String, surname: String, birthYear: Int): Customer = Person(name, surname, birthYear) match
      case person if person.age < 35 =>
        val customer = YoungCustomerImpl(name, surname, birthYear)
        logger.log(logger.getPrefixFormatter().getCreationPrefix + customer)
        customer
      case _ =>
        val customer = BaseCustomerImpl(name, surname, birthYear)
        logger.log(logger.getPrefixFormatter().getCreationPrefix + customer)
        customer