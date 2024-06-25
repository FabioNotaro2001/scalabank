package scalabank.entities

import scalabank.currency.Currency
import scalabank.currency.MoneyADT.Money

trait SavingsJar:
  def amount: Money
  def currency: Currency
  def annualInterest: Double
  def monthlyDeposit(amount: Money): Unit
  def deposit(amount: Money): Unit
  def withdraw(amount: Money): Boolean
  def annualProjection(year: Int): Money
  //def transition: List[Transition]
