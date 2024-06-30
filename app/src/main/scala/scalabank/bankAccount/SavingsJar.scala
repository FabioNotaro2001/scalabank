package scalabank.bankAccount

import scalabank.currency.{Currency, CurrencyConverter}
import scalabank.currency.MoneyADT.Money
import scalabank.currency.MoneyADT.toMoney

import java.time.LocalDate

/**
 * Trait representing a savings jar.
 */
trait SavingsJar:

  /**
   * @return the current balance of the savings jar.
   */
  def balance: Money

  /**
   * @return the currency of the savings jar.
   */
  def currency: Currency

  /**
   * @return the annual interest rate of the savings jar.
   */
  def annualInterest: Double

  /**
   * @return the monthly deposit amount of the savings jar.
   */
  def monthlyDeposit: Money

  /**
   * Sets the annual interest rate of the savings jar.
   *
   * @param interest the new annual interest rate.
   */
  def setAnnualInterest(interest: Double): Unit

  /**
   * Sets the monthly deposit amount of the savings jar.
   *
   * @param amount the new monthly deposit amount.
   */
  def setMonthlyDeposit(amount: Money): Unit

  /**
   * Changes the currency of the savings jar.
   *
   * @param newCurrency   the new currency.
   * @param conversionFee the fee for converting the currency.
   */
  def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit

  /**
   * Deposits a specified amount of money into the savings jar.
   *
   * @param amount the amount of money to deposit.
   * @return true if the deposit was successful, false otherwise.
   */
  def deposit(amount: Money): Boolean

  /**
   * Withdraws a specified amount of money from the savings jar.
   *
   * @param amount the amount of money to withdraw.
   * @return true if the withdrawal was successful, false otherwise.
   */
  def withdraw(amount: Money): Boolean

  /**
   * Applies the monthly deposit to the savings jar.
   *
   * @return true if the monthly deposit was successful, false otherwise.
   */
  def applyMonthlyDeposit(): Boolean

  /**
   * Applies the annual interest to the savings jar.
   *
   * @return true if the annual interest was successfully applied, false otherwise.
   */
  def applyYearInterest(): Boolean

  /**
   * Calculates the projected balance of the savings jar at the end of the specified number of years.
   * The calculation takes into account the remaining months of the current year.
   *
   * @param year the number of years for the projection.
   * @return the projected balance.
   */
  def annualProjection(year: Int): Money

case class SavingJarImpl(var _annualInterest: Double,
                         var _monthlyDeposit: Money,
                         var _currency: Currency,
                         bankAccount: BankAccount) extends SavingsJar:

  private var _balance: Money = 0.toMoney

  override def balance: Money = _balance

  override def currency: Currency = _currency

  override def annualInterest: Double = _annualInterest

  override def monthlyDeposit: Money = _monthlyDeposit

  override def setAnnualInterest(interest: Double): Unit = _annualInterest = interest

  override def setMonthlyDeposit(amount: Money): Unit = _monthlyDeposit = amount

  override def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit =
    val converter = CurrencyConverter()
    _balance = converter.convertWithFee(_balance, _currency, newCurrency)(using conversionFee)
    _currency = newCurrency

  override def deposit(amount: Money): Boolean = amount match
    case am if am <= bankAccount.balance =>
      if bankAccount.withdraw(amount, 0.toMoney) then
        _balance = _balance + amount
        true
      else
        throw IllegalArgumentException()
    case _ => throw IllegalArgumentException()

  override def withdraw(amount: Money): Boolean = amount match
    case am if am <= _balance =>
      _balance = _balance - amount
      bankAccount.deposit(amount, 0.toMoney)
      true
    case _ => throw IllegalArgumentException()

  override def applyYearInterest(): Boolean =
    val newBalance = _balance * (1 + annualInterest / 100)
    if newBalance >= 0.toMoney then
      _balance = newBalance
      return true
    false

  override def applyMonthlyDeposit(): Boolean =
    deposit(monthlyDeposit)

  override def annualProjection(year: Int): Money =
    var projectedBalance = _balance
    val monthsLeft = 12 - LocalDate.now().getMonthValue + 1

    projectedBalance = projectedBalance + (monthlyDeposit * monthsLeft)
    projectedBalance = projectedBalance * (1 + annualInterest / 100)

    for _ <- 2 to year do
      projectedBalance = projectedBalance + (monthlyDeposit * 12)
      projectedBalance = projectedBalance * (1 + annualInterest / 100)
    projectedBalance

/**
 * Companion object for the SavingsJar trait.
 */
object SavingsJar:
  def apply(annualInterest: Double,
            monthlyDeposit: Money,
            currency: Currency,
            bankAccount: BankAccount): SavingsJar = SavingJarImpl(annualInterest, monthlyDeposit, currency, bankAccount)