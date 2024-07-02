package scalabank.entities

import scalabank.entities.Person
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.appointment.AppointmentBehaviour
import scalabank.currency.Currency
import scalabank.bankAccount.{BankAccount, StateBankAccount}
import scalabank.bank.{Bank, BankAccountType}

/**
 * Trait representing a customer of a bank
 */
trait Customer extends Person with AppointmentBehaviour:
  /**
   * Calculates the fidelity level of the customer based on their bank accounts and a fidelity calculator.
   *
   * @return the fidelity level of the customer.
   */
  def fidelity(using FidelityCalculator): FidelityLevel

  /**
   * @return the bank the customer is registered with, if any.
   */
  def bank: Option[Bank]

  /**
   * Registers the customer with a specified bank.
   *
   * @param bank the bank to register with.
   */
  def registerBank(bank: Bank): Unit

  /**
   * Deregisters the customer from their current bank.
   *
   * @param bank the bank to deregister from.
   */
  def deregisterBank(bank: Bank): Unit

  /**
   * Adds an existing bank account to the customer's list of bank accounts.
   *
   * @param bankAccount the bank account to add.
   */
  def addBankAccount(bankAccount: BankAccount): Unit

  /**
   * Creates and adds a new bank account of a specified type and currency to the customer's list of bank accounts.
   *
   * @param bankAccountType the type of the bank account to create.
   * @param currency        the currency of the bank account to create.
   */
  def addBankAccount(bankAccountType: BankAccountType, currency: Currency): Unit

  /**
   * @return the list of bank accounts associated with the customer.
   */
  def bankAccounts: Iterable[BankAccount]

abstract class AbstractCustomer(_cf: String,
                       _name: String,
                       _surname: String,
                       _birthYear: Int) extends Customer:

  private var _bank: Option[Bank] = None
  private var _bankAccounts: List[BankAccount] = List()

  private val person = Person(_cf, _name, _surname, _birthYear)

  export person.*

  protected def points: Int = bankAccounts.map(_.fidelity.points).sum

  override def bank: Option[Bank] = _bank

  override def registerBank(bank: Bank): Unit = _bank match
    case None => _bank = Some(bank)
    case _ =>

  override def deregisterBank(bank: Bank): Unit = bankAccounts match
    case List() => _bank = None
    case ba if ba.map(_.state).map(_.equals(StateBankAccount.Closed)).forall(_ == true) => _bank = None
    case _ => throw IllegalStateException()

  override def addBankAccount(bankAccount: BankAccount): Unit =
    _bankAccounts = _bankAccounts :+ bankAccount
  
  override def addBankAccount(bankAccountType: BankAccountType, currency: Currency): Unit = _bank match
    case Some(bank) =>
      val newBankAccount = bank.createBankAccount(this, bankAccountType, currency)
    case None =>

  override def bankAccounts: Iterable[BankAccount] = _bankAccounts

/**
 * Trait representing a fidelity calculator.
 */
trait FidelityCalculator:
  /**
   * Calculates the fidelity level based on the given points and age group.
   *
   * @param points       the fidelity points.
   * @param isYoungOrOld whether the customer is considered young or old.
   * @return the fidelity level.
   */
  def calculateFidelityLevel(points: Int, isYoungOrOld: Boolean): FidelityLevel

/**
 * Given implementation of the FidelityCalculator.
 */
given defaultFidelityCalculator: FidelityCalculator with
  def calculateFidelityLevel(points: Int, isYoungOrOld: Boolean): FidelityLevel = isYoungOrOld match
    case true => points match
      case p if p >= 1000 => FidelityLevel.Platinum
      case p if p >= 500 => FidelityLevel.Gold
      case p if p >= 250 => FidelityLevel.Silver
      case _ => FidelityLevel.Bronze
    case false => points match
      case p if p >= 2000 => FidelityLevel.Platinum
      case p if p >= 1000 => FidelityLevel.Gold
      case p if p >= 500 => FidelityLevel.Silver
      case _ => FidelityLevel.Bronze

/**
 * Component trait for customer, including logging and different customer types.
 */
trait CustomerComponent:
  loggerDependency: LoggerDependency =>
  case class YoungCustomerImpl(_cf: String,
                               _name: String,
                               _surname: String,
                               _birthYear: Int) extends AbstractCustomer(_cf: String, _name: String, _surname: String, _birthYear: Int):

    override def fidelity(using calc: FidelityCalculator): FidelityLevel = calc.calculateFidelityLevel(points, true)

    loggerDependency.logger.log(logger.getPrefixFormatter.getCreationPrefix + this)

  case class OldCustomerImpl(_cf: String,
                             _name: String,
                             _surname: String,
                             _birthYear: Int) extends AbstractCustomer(_cf: String, _name: String, _surname: String, _birthYear: Int):

    override def fidelity(using calc: FidelityCalculator): FidelityLevel = calc.calculateFidelityLevel(points, true)

    loggerDependency.logger.log(logger.getPrefixFormatter.getCreationPrefix + this)

  case class BaseCustomerImpl(_cf: String,
                              _name: String,
                              _surname: String,
                              _birthYear: Int) extends AbstractCustomer(_cf: String, _name: String, _surname: String, _birthYear: Int):

    override def fidelity(using calc: FidelityCalculator): FidelityLevel = calc.calculateFidelityLevel(points, false)

    loggerDependency.logger.log(logger.getPrefixFormatter.getCreationPrefix + this)

/**
 * Companion object for the Customer trait.
 */
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