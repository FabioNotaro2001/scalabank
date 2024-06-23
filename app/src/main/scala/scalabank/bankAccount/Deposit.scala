package scalabank.bankAccount

import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime

class Deposit(override val value: Money) extends Movement:

  private val _date = LocalDateTime.now()

  override def date: LocalDateTime = _date

  override def toString: String = s"Deposit of $value at ${date.format(super.dateFormatter)}"