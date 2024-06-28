package scalabank.entities

import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.appointment.{Appointment, AppointmentBehaviour}
import scalabank.currency.Currency
import scalabank.bankAccount.BankAccount
import scalabank.bank.{Bank, BankAccountType}

trait Customer extends Person with AppointmentBehaviour:
  def fidelity: Fidelity
  def baseFee(using BaseFeeCalculator): Double
  def bank: Option[Bank]
  def registerBank(bank: Bank): Unit
  def deregisterBank(bank: Bank): Unit
  def addBankAccount(bankAccount: BankAccount): Unit
  def addBankAccount(bankAccountType: BankAccountType, currency: Currency): Unit
  def bankAccounts: Iterable[BankAccount]

abstract class AbstractCustomer(_cf: String,
                       _name: String,
                       _surname: String,
                       _birthYear: Int) extends Customer:

  private var _bank: Option[Bank] = None
  private var _bankAccounts: List[BankAccount] = List()

  private val person = Person(_cf, _name, _surname, _birthYear)

  export person.*

  override def fidelity: Fidelity = Fidelity(0)

  override def bank: Option[Bank] = _bank

  override def registerBank(bank: Bank): Unit = _bank match
    case None => _bank = Some(bank)
    case _ =>

  override def deregisterBank(bank: Bank): Unit = _bank = None

  override def addBankAccount(bankAccount: BankAccount): Unit =
    _bankAccounts = _bankAccounts :+ bankAccount
  
  override def addBankAccount(bankAccountType: BankAccountType, currency: Currency): Unit = _bank match
    case Some(bank) =>
      val newBankAccount = bank.createBankAccount(this, bankAccountType, currency)
    case None =>

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
                               _birthYear: Int) extends AbstractCustomer(_cf: String, _name: String, _surname: String, _birthYear: Int):
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, true)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)


  case class OldCustomerImpl(_cf: String,
                             _name: String,
                             _surname: String,
                             _birthYear: Int) extends AbstractCustomer(_cf: String, _name: String, _surname: String, _birthYear: Int):
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, true)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)


  case class BaseCustomerImpl(_cf: String,
                              _name: String,
                              _surname: String,
                              _birthYear: Int) extends AbstractCustomer(_cf: String, _name: String, _surname: String, _birthYear: Int):
    override def baseFee(using calc: BaseFeeCalculator): Double = calc.calculateBaseFee(fidelity, false)
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)


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