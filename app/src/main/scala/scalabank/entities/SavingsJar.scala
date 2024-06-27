package scalabank.entities

import scalabank.bankAccount.BankAccount
import scalabank.currency.{Currency, CurrencyConverter}
import scalabank.currency.MoneyADT.Money
import scalabank.currency.MoneyADT.toMoney

import java.time.LocalDate

trait SavingsJar:
  def balance: Money
  def currency: Currency
  def annualInterest: Double
  def monthlyDeposit: Money
  def setAnnualInterest(interest: Double): Unit
  def setMonthlyDeposit(amount: Money): Unit
  def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit
  def deposit(amount: Money): Boolean
  def withdraw(amount: Money): Boolean
  def applyMonthlyDeposit(): Unit
  def applyYearInterest(): Unit
  def annualProjection(year: Int): Money

//def transition: List[Transition]

case class SavingJarImpl(var _annualInterest: Double,
                         var _monthlyDeposit: Money,
                         var _currency: Currency,
                         val bankAccount: BankAccount) extends SavingsJar:

  var _balance: Money = 0.toMoney

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
        false
    case _ => false

  override def withdraw(amount: Money): Boolean = amount match
    case am if am <= _balance =>
      _balance = _balance - amount
      bankAccount.deposit(amount, 0.toMoney)
      true
    case _ => false

  override def applyYearInterest(): Unit =
    _balance = _balance * (1 + annualInterest / 100)

  override def applyMonthlyDeposit(): Unit =
    withdraw(monthlyDeposit)

  override def annualProjection(year: Int): Money =
    var projectedBalance = _balance
    val monthsLeft = 12 - LocalDate.now().getMonthValue + 1

    projectedBalance = projectedBalance + (monthlyDeposit * monthsLeft)
    projectedBalance = projectedBalance * (1 + annualInterest / 100)

    for _ <- 2 to year do
      projectedBalance = projectedBalance + (monthlyDeposit * 12)
      projectedBalance = projectedBalance * (1 + annualInterest / 100)
    projectedBalance

object SavingsJar:
  def apply(annualInterest: Double,
            monthlyDeposit: Money,
            currency: Currency,
            bankAccount: BankAccount): SavingsJar = SavingJarImpl(annualInterest, monthlyDeposit, currency, bankAccount)