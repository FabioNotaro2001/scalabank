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
     * Set the new balance.
     * @param newBalance the new balance.
     */
    def setBalance(newBalance: Money): Unit

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
                                override val id: Int,
                                override val customer: Customer,
                                var balance: Money,
                                override val currency: Currency,
                                override val state: StateBankAccount,
                                override val bankAccountType: BankAccountType,
                              ) extends BankAccount:
        loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getPrefixForBankAccountOpening + this)

        private var _movements: List[Movement] = List()

        override def setBalance(newBalance: Money): Unit = balance = newBalance

        override def movements: SeqView[Movement] = _movements.view

        override def deposit(amount: Money): Unit =
            val depositInstance = Deposit(this, amount)
            depositInstance.doOperation()
            _movements = _movements :+ depositInstance
            loggerDependency.logger.log(depositInstance.toString)

        override def withdraw(amount: Money): Boolean =
            val withdraw = Withdraw(this, amount, bankAccountType.feePerOperation)
            val result = withdraw.doOperation()
            if result then
                _movements = _movements :+ withdraw
                loggerDependency.logger.log(withdraw.toString)
            result

        override def makeMoneyTransfer(senderBankAccount: BankAccount, receiverBankAccount: BankAccount, amount: Money): Boolean =
            val moneyTransferInstance = MoneyTransfer(senderBankAccount, receiverBankAccount, amount)
            val result = moneyTransferInstance.doOperation()
            if result then
                _movements = _movements :+ moneyTransferInstance
                loggerDependency.logger.log(moneyTransferInstance.toString)  // TODO: cambiare logger e prefisso.
            result