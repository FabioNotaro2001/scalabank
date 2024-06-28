package scalabank.bankAccount

import scalabank.currency.FeeManager
import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

/**
 * Represents a withdrawal movement from a bank account.
 * @param receiverBankAccount the bank account from which the withdrawal is made
 * @param value               the amount of money being withdrawn
 * @param fee                 the fee associated with the withdrawal
 * @param date                the date and time of the withdrawal (default is the current date and time)
 * @param senderBankAccount   the bank account sending the withdrawal (default is null)
 */
case class Withdraw(override val receiverBankAccount: BankAccount, override val value: Money, override val fee: Money, override val date: LocalDateTime = LocalDateTime.now(), override val senderBankAccount: BankAccount = null) extends Movement:

  override def doOperation(): Boolean =
    val amountWithFee = FeeManager.calculateAmountWithFee(value, fee)
    if receiverBankAccount.balance >= amountWithFee then
      receiverBankAccount.setBalance(receiverBankAccount.balance - amountWithFee)
      true
    else false

  override def toString: String = s"[${date.format(super.dateFormatter)}] Withdraw of $value with a fee of: $fee on bank account ${receiverBankAccount.id}"