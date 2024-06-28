package scalabank.bankAccount

import scalabank.bank.BankAccountType
import scalabank.currency.MoneyADT.Money
import scalabank.currency.{Currency, CurrencyConverter, FeeManager, MoneyADT}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.entities.*

import scala.collection.SeqView
import scala.collection.immutable.List
import scala.reflect.ClassTag

import scalabank.currency.MoneyADT.toMoney

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

    def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit

    /**
     * @return the current state of the bank account
     */
    def state: StateBankAccount

    def setState(stateBankAccount: StateBankAccount): Unit

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
    def deposit(amount: Money, fee: Money = bankAccountType.feeDeposit): Unit

    /**
     * Withdraws a specified amount of money from the bank account if there is enough money.
     *
     * @param amount the amount of money to withdraw
     * @return the result of the operation
     */
    def withdraw(amount: Money, fee: Money = bankAccountType.feeDeposit): Boolean
    
    /**
     * Filter account transactions based on the specified type.
     *
     * @tparam T The type of motion to filter.
     * @return A view of movements that are of the specified type.
     */
    def filterMovements[T <: Movement : ClassTag]: SeqView[T]
    
    /**
     * Makes a money transfer to a specified bank account.
     *
     * @param receiverBankAccount is the bank account of the receiver.
     * @return the result of the operation.
     */
    def makeMoneyTransfer(receiverBankAccount: BankAccount, amount: Money): Boolean

    /**
     * Registers a money transfer to this account.
     *
     * @param senderBankAccount is the bank account of the sender.
     */
    def receiveMoneyTransfer(senderBankAccount: BankAccount, amount: Money): Unit

    /**
     * Adds a movement to the list of movements for the account
     * @param movement the movement to be added
     */
    def addMovement(movement: Movement): Unit

    def savingsJar: Option[SavingsJar]

    def createSavingJar(monthlyDeposit: Money, annualInterest: Double = bankAccountType.interestSavingJar): Unit

    def fidelity: Fidelity

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
                                var currency: Currency,
                                var state: StateBankAccount,
                                override val bankAccountType: BankAccountType
                              ) extends BankAccount:
        loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter.getPrefixForBankAccountOpening + this)

        private var _movements: List[Movement] = List()
        private var _savingsJar: Option[SavingsJar] = Option.empty
        private var _fidelity: Fidelity = Fidelity(0)

        override def setBalance(newBalance: Money): Unit = balance = newBalance

        override def changeCurrency(newCurrency: Currency, conversionFee: BigDecimal): Unit =
            val converter = CurrencyConverter()
            balance = converter.convertWithFee(balance, currency, newCurrency)(using conversionFee)
            currency = newCurrency

        override def setState(stateBankAccount: StateBankAccount): Unit =
            state = stateBankAccount

        override def savingsJar: Option[SavingsJar] = _savingsJar

        override def createSavingJar(monthlyDeposit: Money, annualInterest: Double = bankAccountType.interestSavingJar): Unit = _savingsJar =
            Some(SavingsJar(annualInterest, monthlyDeposit, currency, this))
        
        override def movements: SeqView[Movement] = _movements.view

        override def filterMovements[T <: Movement : ClassTag]: SeqView[T] =
            _movements.collect { case m: T => m }.view

        import bankAccountType.*
        override def deposit(amount: Money, fee: Money = feeDeposit): Unit =
            _fidelity.addPoints(100)
            val depositInstance = Deposit(this, amount, fee)
            depositInstance.doOperation()
            _movements = _movements :+ depositInstance
            loggerDependency.logger.log(logger.getPrefixFormatter.getPrefixForDeposit + depositInstance.toString)
        
        override def withdraw(amount: Money, fee: Money = feeWithdraw): Boolean =
            val withdraw = Withdraw(this, amount, fee)
            val result = withdraw.doOperation()
            if result then
                _movements = _movements :+ withdraw
                loggerDependency.logger.log(logger.getPrefixFormatter.getPrefixForWithdraw + withdraw.toString)
            result

        override def makeMoneyTransfer(receiverBankAccount: BankAccount, amount: Money): Boolean =
            val moneyTransferInstance = MoneyTransfer(this, receiverBankAccount, amount, bankAccountType.feeMoneyTransfert)
            val result = moneyTransferInstance.doOperation()
            if result then
                addMovement(moneyTransferInstance)
                receiverBankAccount.receiveMoneyTransfer(this, amount)
                loggerDependency.logger.log(logger.getPrefixFormatter.getPrefixForMoneyTransfer + moneyTransferInstance.toString)
            result

        override def receiveMoneyTransfer(senderBankAccount: BankAccount, amount: Money): Unit =
            val moneyTransferInstance = MoneyTransfer(senderBankAccount, this, amount, bankAccountType.feeMoneyTransfert)
            addMovement(moneyTransferInstance)
            loggerDependency.logger.log(logger.getPrefixFormatter.getPrefixForMoneyTransfer + moneyTransferInstance.toString)

        override def addMovement(movement: Movement): Unit =
            _movements = _movements :+ movement

        override def fidelity: Fidelity = _fidelity
