package scalabank.bankAccount

import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait Movement:
  def value: Money
  def date: LocalDateTime
  def dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
