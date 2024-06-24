package scalabank.bankAccount

import scalabank.currency.FeeManager
import scalabank.currency.MoneyADT.Money
import java.time.LocalDateTime

/**
 * Represents a money transfer movement between two bank accounts.
 *
 * @constructor Creates a new money transfer with the specified sender and receiver bank accounts, and transfer value.
 * @param senderBankAccount the bank account from which the money is transferred.
 * @param receiverBankAccount the bank account to which the money is transferred.
 * @param value the amount of money being transferred.
 */
class MoneyTransfer(senderBankAccount: BankAccount, override val receiverBankAccount: BankAccount, override val value: Money) extends Movement:

  private val emissionDate = LocalDateTime.now()

  /**
   * Gets the date and time when the money transfer was initiated.
   *
   * @return the date and time of the money transfer.
   */
  override def date: LocalDateTime = emissionDate

  /**
   * Provides a string representation of the money transfer.
   *
   * @return a string describing the money transfer.
   */
  override def toString: String = s"Money transfer of $value at ${date.format(super.dateFormatter)} between bank account ${senderBankAccount.id} and ${receiverBankAccount.id}"

  /**
   * Executes the money transfer operation.
   *
   * Deducts the transfer value plus the fee from the sender bank account's balance and adds the transfer value to the receiver bank account's balance.
   *
   * @return true if the operation is successful, false otherwise.
   */
  override def doOperation(): Boolean =
    val feePerMoneyTransfer = senderBankAccount.bankAccountType.feePerOperation
    val amountWithFee = FeeManager.calculateAmountWithFee(value, feePerMoneyTransfer)
    if senderBankAccount.balance >= amountWithFee then
      senderBankAccount.setBalance(senderBankAccount.balance - amountWithFee)
      receiverBankAccount.setBalance(receiverBankAccount.balance + value)
      true
    else false
