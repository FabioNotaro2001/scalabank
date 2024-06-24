package scalabank.bankAccount

import scalabank.currency.FeeManager
import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

class Withdraw(override val receiverBankAccount: BankAccount, override val value: Money, fee: Money) extends Movement:

  private val _date = LocalDateTime.now()

  override def date: LocalDateTime = _date

  override def doOperation(): Boolean =
    val amountWithFee = FeeManager.calculateAmountWithFee(value, fee)
    if receiverBankAccount.balance >= amountWithFee then
      receiverBankAccount.setBalance(receiverBankAccount.balance - amountWithFee)
      true
    else false

  override def toString: String = s"Withdraw of $value with a fee of: $fee at ${date.format(super.dateFormatter)} on bank account ${receiverBankAccount.id}"