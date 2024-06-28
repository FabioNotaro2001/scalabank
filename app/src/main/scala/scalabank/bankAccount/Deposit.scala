package scalabank.bankAccount

import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

/**
 * Represents a deposit movement in a bank account.
 * @param receiverBankAccount the bank account receiving the deposit
 * @param value               the amount of money being deposited
 * @param fee                 the fee associated with the deposit
 * @param date                the date and time of the deposit (default is the current date and time)
 * @param senderBankAccount   the bank account sending the deposit (default is null)
 */
case class Deposit(override val receiverBankAccount: BankAccount, override val value: Money, override val fee: Money, override val date: LocalDateTime = LocalDateTime.now(), override val senderBankAccount: BankAccount = null) extends Movement:

  override def doOperation(): Boolean =
    receiverBankAccount.setBalance(receiverBankAccount.balance + value)
    true

  override def toString: String = s"[${date.format(super.dateFormatter)}] Deposit of $value on bank account ${receiverBankAccount.id}"