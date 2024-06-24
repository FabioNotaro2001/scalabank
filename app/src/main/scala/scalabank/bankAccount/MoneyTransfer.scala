package scalabank.bankAccount

import scalabank.currency.FeeManager
import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

class MoneyTransfer(senderBankAccount: BankAccount, override val receiverBankAccount: BankAccount, override val value: Money) extends Movement:
  private val emissionDate = LocalDateTime.now()
  override def date: LocalDateTime = emissionDate
  override def toString: String = s"Money transfer of $value at ${date.format(super.dateFormatter)} between bank account ${senderBankAccount.id} and ${receiverBankAccount.id}"
  override def doOperation(): Boolean =
    val feePerMoneyTransfer = senderBankAccount.bankAccountType.feePerOperation
    val amountWithFee = FeeManager.calculateAmountWithFee(value, feePerMoneyTransfer)
    if senderBankAccount.balance >= amountWithFee then
      senderBankAccount.setBalance(senderBankAccount.balance - amountWithFee)
      receiverBankAccount.setBalance(receiverBankAccount.balance + value)
      true
    else false