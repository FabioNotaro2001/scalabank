package scalabank.bankAccount

import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

case class Deposit(override val receiverBankAccount: BankAccount, override val value: Money, override val date: LocalDateTime = LocalDateTime.now(), override val senderBankAccount: BankAccount = null) extends Movement:

  override def doOperation(): Boolean =
    receiverBankAccount.setBalance(receiverBankAccount.balance + value)
    true

  override def toString: String = s"[${date.format(super.dateFormatter)}] Deposit of $value on bank account ${receiverBankAccount.id}"