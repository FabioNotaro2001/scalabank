package scalabank.bankAccount

import scalabank.currency.FeeManager
import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

class MoneyTransfer(senderBankAccount: BankAccount, receiverBankAccount: BankAccount, override val value: Money) extends Movement:
  private val emissionDate = LocalDateTime.now()
  override def date: LocalDateTime = emissionDate
  override def toString: String = s"Money transfer of $value at ${date.format(super.dateFormatter)} between ${senderBankAccount.id} and ${receiverBankAccount.id}"
  override def doOperation(): Boolean = true
    /*val feePerMoneyTransfer = senderBankAccount.bankAccountType.feePerOperation
    val amountWithFee = FeeManager.calculateAmountWithFee(value, feePerMoneyTransfer)
    if senderBankAccount.balance >= amountWithFee then
      senderBankAccount.balance = senderBankAccount.balance - amountWithFee
      receiverBankAccount.balance = receiverBankAccount.balance + value
      true
    false*/