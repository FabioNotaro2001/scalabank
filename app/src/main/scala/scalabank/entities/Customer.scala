package scalabank.entities

import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.appointment.Appointment

trait Customer extends Person:
  def fidelity: Fidelity
  def baseFee(using BaseFeeCalculator): Double
  def getAppointments(): Iterable[Appointment]
  def addAppointment(appointment: Appointment): Unit
  def removeAppointment(appointment: Appointment): Unit
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit

trait YoungCustomer extends Customer with CustomerBehaviour

trait BaseCustomer extends Customer with CustomerBehaviour

trait CustomerBehaviour: 
  private var appointments: List[Appointment] = List()
  def fidelity: Fidelity = Fidelity(0)
  def getAppointments(): Iterable[Appointment] = appointments
  def addAppointment(appointment: Appointment): Unit = appointments = appointments :+ appointment
  def removeAppointment(appointment: Appointment): Unit = appointments = appointments.filterNot(_ == appointment)
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app

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
                               _birthYear: Int) extends YoungCustomer:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, true)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    private val person = Person(_name, _surname, _birthYear)
    export person.*

  case class BaseCustomerImpl(_name: String,
                              _surname: String,
                              _birthYear: Int) extends BaseCustomer:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, false)
    logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    private val person = Person(_name, _surname, _birthYear)
    export person.*




object Customer extends LoggerDependency with CustomerComponent:
  override val logger: Logger = LoggerImpl()
  def apply(name: String, surname: String, birthYear: Int): Customer = Person(name, surname, birthYear) match
      case person if person.age < 35 =>
        val customer = YoungCustomerImpl(name, surname, birthYear)
        customer
      case _ =>
        val customer = BaseCustomerImpl(name, surname, birthYear)
        customer