package scalabank.bank

import scalabank.currency.MoneyADT.Money

/**
 * Represents a bank account type with a name and description.
 */
trait BankAccountType:
  def nameType: String
  def feeWithdraw: Money
  def feeDeposit: Money
  def feeMoneyTransfert: Money
  def interestSavingJar: Double


object BankAccountType:

  def apply(nameType: String, feeWithdraw: Money, feeDeposit: Money, feeMoneyTransfert: Money, interestSavingJar: Double): BankAccountType = BankAccountTypeImpl(nameType, feeWithdraw, feeDeposit, feeMoneyTransfert, interestSavingJar)

  private case class BankAccountTypeImpl(override val nameType: String,
                                         override val feeWithdraw: Money,
                                         override val feeDeposit: Money,
                                         override val feeMoneyTransfert: Money,
                                         override val interestSavingJar: Double) extends BankAccountType



