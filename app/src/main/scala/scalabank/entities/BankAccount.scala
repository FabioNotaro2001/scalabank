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

trait BankAccountBehavior extends BankAccount:
    val _id: Int
    val _balance: Money
    val _currency: Currency
    val _state: StateBankAccount
    val loggerDependency: LoggerDependency

    loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getPrefixForBankAccountOpening + this)

    override def id: Int = _id
    override def balance: Money = _balance
    override def currency: Currency = _currency
    override def state: StateBankAccount = _state

    override def computeFee: Money = ???
    override def computeInterest: Money = ???

object BaseBankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, balance: Money, currency: Currency, state: StateBankAccount): BaseBankAccountImpl =
        BaseBankAccountImpl(id, balance, currency, state, this)

object SuperBankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, balance: Money, currency: Currency, state: StateBankAccount): SuperBankAccountImpl =
        SuperBankAccountImpl(id, balance, currency, state, this)

trait BankAccountComponent:
    loggerDependency: LoggerDependency =>

    case class BaseBankAccountImpl(
                                    _id: Int,
                                    _balance: Money,
                                    _currency: Currency,
                                    _state: StateBankAccount,
                                    loggerDependency: LoggerDependency
                                  ) extends BankAccount with BankAccountBehavior

    case class SuperBankAccountImpl(
                                     _id: Int,
                                     _balance: Money,
                                     _currency: Currency,
                                     _state: StateBankAccount,
                                     loggerDependency: LoggerDependency
                                   ) extends BankAccount with BankAccountBehavior
