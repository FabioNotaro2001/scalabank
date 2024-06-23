package scalabank.bankAccount

import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

class Withdraw(override val value: Money, fee: Money) extends Movement:

  private val _date = LocalDateTime.now()

  override def date: LocalDateTime = _date

  override def toString: String = s"Withdraw of $value with a fee of: $fee at ${date.format(super.dateFormatter)}"