package scalabank.entities

import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.appointment.Appointment
import scalabank.currency.MoneyADT
import scalabank.currency.MoneyADT.toMoney
import scalabank.currency.Currency
import scalabank.entities.StateBankAccount.Active

trait Customer extends Person:
  def fidelity: Fidelity
  def baseFee(using BaseFeeCalculator): Double
  def getAppointments: Iterable[Appointment]
  def addAppointment(appointment: Appointment): Unit
  def removeAppointment(appointment: Appointment): Unit
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit
  def createBaseBankAccount(_id : Int): Unit
  def createSuperBankAccount(_id : Int): Unit
  def bankAccount: Option[BankAccount]


trait YoungCustomer extends Customer with CustomerBehaviour

trait OldCustomer extends Customer with CustomerBehaviour

trait BaseCustomer extends Customer with CustomerBehaviour

trait CustomerBehaviour:
  private var appointments: List[Appointment] = List()
  private var _bankAccount: Option[BankAccount] = None
  def fidelity: Fidelity = Fidelity(0)
  def getAppointments: Iterable[Appointment] = appointments
  def addAppointment(appointment: Appointment): Unit = appointments = appointments :+ appointment
  def removeAppointment(appointment: Appointment): Unit = appointments = appointments.filterNot(_ == appointment)
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app
  def createBaseBankAccount(_id : Int): Unit = _bankAccount = Some(BaseBankAccount(id = _id, balance = 100.toMoney, currency = Currency("EUR", "€"), state = Active));
  def createSuperBankAccount(_id : Int): Unit = _bankAccount = Some(SuperBankAccount(id = _id, balance = 100.toMoney, currency = Currency("EUR", "€"), state = Active));
  def bankAccount: Option[BankAccount] = _bankAccount


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
  case class YoungCustomerImpl(_cf: String,
                               _name: String,
                               _surname: String,
                               _birthYear: Int) extends YoungCustomer:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, true)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    private val person = Person(_cf, _name, _surname, _birthYear)
    export person.*

  case class OldCustomerImpl( _cf: String,
                              _name: String,
                              _surname: String,
                              _birthYear: Int) extends OldCustomer:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, true)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    private val person = Person(_cf, _name, _surname, _birthYear)
    export person.*

  case class BaseCustomerImpl(_cf: String,
                              _name: String,
                              _surname: String,
                              _birthYear: Int) extends BaseCustomer:
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, false)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    private val person = Person(_cf, _name, _surname, _birthYear)
    export person.*

object Customer extends LoggerDependency with CustomerComponent:
  override val logger: Logger = LoggerImpl()
  def apply(cf: String, name: String, surname: String, birthYear: Int): Customer = Person(cf, name, surname, birthYear) match
    case person if person.age < 35 =>
      val customer = YoungCustomerImpl(cf, name, surname, birthYear)
      customer
    case person if person.age > 65 =>
      val customer = OldCustomerImpl(cf, name, surname, birthYear)
      customer
    case _ =>
      val customer = BaseCustomerImpl(cf, name, surname, birthYear)
      customer