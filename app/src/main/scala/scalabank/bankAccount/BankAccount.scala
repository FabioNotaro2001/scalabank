package scalabank.bankAccount

import scalabank.bank.BankAccountType
import scalabank.currency.MoneyADT.Money
import scalabank.currency.{Currency, FeeManager, MoneyADT}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.entities.*
import scala.collection.SeqView
import scala.collection.immutable.List

/**
 * Represents the state of a bank account.
 */
enum StateBankAccount:
    case Active, Inactive, Closed

/**
 * Trait representing a bank account.
 */
trait BankAccount:
    /**
     * @return the unique identifier of the bank account
     */
    def id: Int

    /**
     * @return the customer who owns the bank account
     */
    def customer: Customer

    /**
     * @return the current balance of the bank account
     */
    def balance: Money

    /**
     * @return the currency of the bank account
     */
    def currency: Currency

    /**
     * @return the current state of the bank account
     */
    def state: StateBankAccount

    /**
     * @return the type of the bank account
     */
    def bankAccountType: BankAccountType

    /**
     * @return a view of the movements (transactions) associated with the bank account
     */
    def movements: SeqView[Movement]

    /**
     * Deposits a specified amount of money into the bank account.
     *
     * @param amount the amount of money to deposit
     */
    def deposit(amount: Money): Unit

    /**
     * Withdraws a specified amount of money from the bank account if there is enough money.
     *
     * @param amount the amount of money to withdraw
     * @return the result of the operation
     */
    def withdraw(amount: Money): Boolean
    def makeMoneyTransfer(senderBankAccount: BankAccount, receiverBankAccount: BankAccount, amount: Money): Boolean


object BankAccount extends LoggerDependency with BankAccountComponent:
    override val logger: Logger = LoggerImpl()
    def apply(id: Int, customer: Customer, balance: Money, currency: Currency, state: StateBankAccount, bankAccountType: BankAccountType): BankAccountImpl =
        BankAccountImpl(id, customer, balance, currency, state, bankAccountType)

/**
 * Component trait for BankAccount, defining the concrete implementation of the BankAccount trait.
 */
trait BankAccountComponent:
    loggerDependency: LoggerDependency =>

    case class BankAccountImpl(
                                _id: Int,
                                _customer: Customer,
                                override var balance: Money,
                                _currency: Currency,
                                _state: StateBankAccount,
                                _bankAccountType: BankAccountType,
                              ) extends BankAccount:
        loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getPrefixForBankAccountOpening + this)

        private var _movements: List[Movement] = List()

        override def id: Int = _id

        override def customer: Customer = _customer

        override def currency: Currency = _currency

        override def state: StateBankAccount = _state

        override def bankAccountType: BankAccountType = _bankAccountType

        override def movements: SeqView[Movement] = _movements.view

        override def deposit(amount: Money): Unit =
            _balance = _balance + amount
            val deposit = Deposit(amount)
            _movements = _movements :+ deposit
            loggerDependency.logger.log(deposit.toString)

        override def withdraw(amount: Money): Boolean =
            val feePerOperation = _bankAccountType.feePerOperation
            val amountWithFee = FeeManager.calculateAmountWithFee(amount, feePerOperation)
            if _balance >= amountWithFee then
                _balance = _balance - amountWithFee
                val withdraw = Withdraw(amount, feePerOperation)
                _movements = _movements :+ withdraw
                loggerDependency.logger.log(withdraw.toString)
                true
            else false

        override def makeMoneyTransfer(senderBankAccount: BankAccount, receiverBankAccount: BankAccount, amount: Money): Boolean = true
