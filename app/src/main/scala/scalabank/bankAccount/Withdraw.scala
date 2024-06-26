package scalabank.bankAccount

import scalabank.currency.FeeManager
import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

case class Withdraw(override val receiverBankAccount: BankAccount, override val value: Money, override val date: LocalDateTime = LocalDateTime.now(), override val senderBankAccount: BankAccount = null) extends Movement:

  private val fee: Money = receiverBankAccount.bankAccountType.feePerOperation

  override def doOperation(): Boolean =
    val amountWithFee = FeeManager.calculateAmountWithFee(value, fee)
    if receiverBankAccount.balance >= amountWithFee then
      receiverBankAccount.setBalance(receiverBankAccount.balance - amountWithFee)
      true
    else false

  override def toString: String = s"[${date.format(super.dateFormatter)}] Withdraw of $value with a fee of: $fee on bank account ${receiverBankAccount.id}"