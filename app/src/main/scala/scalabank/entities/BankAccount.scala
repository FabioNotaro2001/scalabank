package scalabank.entities

import scalabank.currency.MoneyADT.Money
import scalabank.currency.MoneyADT
import scalabank.currency.Currency
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}


enum StateBankAccount:
    case Active, Disactive, Closed

trait BankAccount:
    def id: Int
    def balance: Money
    def currency: Currency
    def state: StateBankAccount
    def computeFee: Money
    def computeInterest: Money

object BaseBankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, balance: Money, currency: Currency, state: StateBankAccount): BaseBankAccountImpl = BaseBankAccountImpl(id, balance, currency, state)

object SuperBankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, balance: Money, currency: Currency, state: StateBankAccount): SuperBankAccountImpl = SuperBankAccountImpl(id, balance, currency, state)

trait BankAccountComponent:
    loggerDependency: LoggerDependency =>
    case class SuperBankAccountImpl(val _id: Int,
                                   val _balance: Money,
                                   val _currency: Currency,
                                   val _state: StateBankAccount) extends BankAccount:

        loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)

        override def id: Int = _id

        override def balance: Money = _balance

        override def currency: Currency = _currency

        override def state: StateBankAccount = _state

        override def computeFee: Money = ???

        override def computeInterest: Money = ???

    case class BaseBankAccountImpl(val _id: Int,
                                   val _balance: Money,
                                   val _currency: Currency,
                                   val _state: StateBankAccount) extends BankAccount:

        loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)

        override def id: Int = _id

        override def balance: Money = _balance

        override def currency: Currency = _currency

        override def state: StateBankAccount = _state

        override def computeFee: Money = ???

        override def computeInterest: Money = ???
