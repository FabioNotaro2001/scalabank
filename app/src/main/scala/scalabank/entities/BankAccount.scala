package scalabank.entities

import scalabank.currency.MoneyADT.Money
import scalabank.currency.MoneyADT
import scalabank.currency.Currency
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

enum StateBankAccount:
    case Active, Inactive, Closed

trait BankAccount:
    def id: Int
    def customer: Customer
    def balance: Money
    def currency: Currency
    def state: StateBankAccount
    def computeFee: Money
    def computeInterest: Money

object BankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, customer: Customer, balance: Money, currency: Currency, state: StateBankAccount): BankAccountImpl =
        BankAccountImpl(id, customer, balance, currency, state)

trait BankAccountComponent:
    loggerDependency: LoggerDependency =>

    case class BankAccountImpl(
                                _id: Int,
                                _customer: Customer,
                                _balance: Money,
                                _currency: Currency,
                                _state: StateBankAccount,
                              ) extends BankAccount:
        override def id: Int = _id

        override def customer: Customer = _customer

        override def balance: Money = _balance

        override def currency: Currency = _currency

        override def state: StateBankAccount = _state

        override def computeFee: Money = ???

        override def computeInterest: Money = ???

        loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getPrefixForBankAccountOpening + this)

