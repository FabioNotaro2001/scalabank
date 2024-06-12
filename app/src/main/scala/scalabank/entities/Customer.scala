package scalabank.entities

import scalabank.entities.Employee.logger
import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

trait Customer extends Person:
  def fidelity: Fidelity
  def baseFee(using BaseFeeCalculator): Double

trait YoungCustomer extends Customer

trait BaseCustomer extends Customer

trait CustomerBehaviour: 
  def fidelity: Fidelity = Fidelity(0)

trait BaseFeeCalculator:
  def calculateBaseFee(fidelity: Fidelity, isYoung: Boolean): Double

given defaultBaseFeeCalculator: BaseFeeCalculator with
   def calculateBaseFee(fidelity: Fidelity, isYoung: Boolean): Double = isYoung match
    case true => 0
    case false => fidelity.currentLevel match
      case level if level == FidelityLevel.Bronze => 1
      case level if level == FidelityLevel.Silver => 0.8
      case level if level == FidelityLevel.Gold => 0.6
      case _ => 0.4

trait CustomerComponent:
  loggerDependency: LoggerDependency =>
  case class YoungCustomerImpl(_name: String,
                               _surname: String,
                               _birthYear: Int) extends YoungCustomer with CustomerBehaviour:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, true)
    
    private val person = Person(_name, _surname, _birthYear)
    export person.*

  case class BaseCustomerImpl(_name: String,
                              _surname: String,
                              _birthYear: Int) extends BaseCustomer with CustomerBehaviour:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, false)

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