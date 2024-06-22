package scalabank.entities

import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.appointment.Appointment
import scalabank.currency.MoneyADT
import scalabank.currency.MoneyADT.toMoney
import scalabank.currency.Currency
import scalabank.entities.StateBankAccount.Active
import scalabank.bank.{Bank, BankAccountType}
import scalabank.bank.Bank.VirtualBank
import scalabank.bank.Bank.PhysicalBank


trait Customer extends Person:
  def fidelity: Fidelity
  def baseFee(using BaseFeeCalculator): Double
  def getAppointments: Iterable[Appointment]
  def addAppointment(appointment: Appointment): Unit
  def removeAppointment(appointment: Appointment): Unit
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit
  def bank: Option[Bank]
  def registerBank(bank: Bank): Unit
  def deregisterBank(bank: Bank): Unit
  def addBankAccount(bankAccount: BankAccount): Unit
  def bankAccounts: Iterable[BankAccount]


trait YoungCustomer extends Customer with CustomerBehaviour

trait OldCustomer extends Customer with CustomerBehaviour

trait BaseCustomer extends Customer with CustomerBehaviour

trait CustomerBehaviour extends Customer:

  private var appointments: List[Appointment] = List()
  private var _bank: Option[Bank] = None
  private var _bankAccounts: List[BankAccount] = List()

  override def fidelity: Fidelity = Fidelity(0)

  override def getAppointments: Iterable[Appointment] = appointments

  override def addAppointment(appointment: Appointment): Unit = appointments = appointments :+ appointment

  override def removeAppointment(appointment: Appointment): Unit = appointments = appointments.filterNot(_ == appointment)

  override def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app

  override def bank: Option[Bank] = _bank

  override def registerBank(bank: Bank): Unit = _bank match
    case None => _bank = Some(bank)
    case _ =>

  override def deregisterBank(bank: Bank): Unit = _bank = None

  override def addBankAccount(bankAccount: BankAccount, bankAccountType: BankAccountType, currency: Currency): Unit = _bank match
    case Bank => _bankAccounts :+ _bank.get.addBankAccount(this, bankAccountType , currency)
    case _ =>

  override def bankAccounts: Iterable[BankAccount] = _bankAccounts


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