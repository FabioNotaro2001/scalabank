package scalabank.entities

import scalabank.bank.BankAccountType
import scalabank.currency.MoneyADT.Money
import scalabank.currency.{Currency, CurrencyConverter, MoneyADT}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

enum StateBankAccount:
    case Active, Inactive, Closed

trait BankAccount:
    def id: Int
    def customer: Customer
    def balance: Money
    def currency: Currency
    def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit
    def state: StateBankAccount
    def setState(stateBankAccount: StateBankAccount): Unit
    def bankAccountType: BankAccountType
    def savingsJar: Option[SavingsJar]
    def createSavingJar(annualInterest: Double, monthlyDeposit: Money): Unit

object BankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, customer: Customer, balance: Money, currency: Currency, state: StateBankAccount, bankAccountType: BankAccountType): BankAccountImpl =
        BankAccountImpl(id, customer, balance, currency, state, bankAccountType)

trait BankAccountComponent:
    loggerDependency: LoggerDependency =>

    case class BankAccountImpl(
                                _id: Int,
                                _customer: Customer,
                                var _balance: Money,
                                var _currency: Currency,
                                var _state: StateBankAccount,
                                _bankAccountType: BankAccountType
                              ) extends BankAccount:

        var _savingsJar: Option[SavingsJar] = Option.empty

        override def id: Int = _id

        override def customer: Customer = _customer

        override def balance: Money = _balance

        override def currency: Currency = _currency

        override def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit =
            val converter = CurrencyConverter()
            _balance = converter.convertWithFee(_balance, _currency, newCurrency)(using conversionFee)
            _currency = newCurrency

        override def state: StateBankAccount = _state

        def setState(stateBankAccount: StateBankAccount): Unit =
            _state = stateBankAccount

        override def bankAccountType: BankAccountType = _bankAccountType

        override def savingsJar: Option[SavingsJar] = _savingsJar

        def createSavingJar(annualInterest: Double, monthlyDeposit: Money): Unit = _savingsJar =
            Some(SavingsJar(annualInterest, monthlyDeposit, currency))

        loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getPrefixForBankAccountOpening + this)

