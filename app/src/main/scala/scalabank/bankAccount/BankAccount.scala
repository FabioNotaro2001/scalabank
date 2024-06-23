package scalabank.bankAccount

import scalabank.bank.BankAccountType
import scalabank.bankAccount.Movement
import scalabank.currency.MoneyADT.Money
import scalabank.currency.{Currency, FeeManager, MoneyADT}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.currency.MoneyADT.toMoney
import scalabank.entities.*

import scala.collection.SeqView
import scala.collection.immutable.List

enum StateBankAccount:
    case Active, Inactive, Closed

trait BankAccount:
    def id: Int
    def customer: Customer
    def balance: Money
    def currency: Currency
    def state: StateBankAccount
    def bankAccountType: BankAccountType
    def movements: SeqView[Movement]
    def deposit(amount: Money): Unit
    def withdraw(amount: Money): Unit


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
                                _currency: Currency,
                                _state: StateBankAccount,
                                _bankAccountType: BankAccountType,
                              ) extends BankAccount:
        loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getPrefixForBankAccountOpening + this)

        private var _movements: List[Movement] = List()

        override def id: Int = _id

        override def customer: Customer = _customer

        override def balance: Money = _balance

        override def currency: Currency = _currency

        override def state: StateBankAccount = _state

        override def bankAccountType: BankAccountType = _bankAccountType

        override def movements: SeqView[Movement] = _movements.view

        override def deposit(amount: Money): Unit =
            _balance = _balance + amount
            val deposit = Deposit(amount)
            _movements = _movements :+ deposit
            loggerDependency.logger.log(deposit.toString)

        override def withdraw(amount: Money): Unit =
            val feePerOperation = _bankAccountType.feeXOperation
            val amountWithFee = FeeManager.calculateAmountWithFee(amount, feePerOperation)
            require(_balance >= amountWithFee)
            _balance = _balance - amountWithFee
            val withdraw = Withdraw(amount, feePerOperation)
            _movements = _movements :+ withdraw
            loggerDependency.logger.log(withdraw.toString)






