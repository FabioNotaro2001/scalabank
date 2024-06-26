package scalabank.bank

import scalabank.currency.MoneyADT.Money

/**
 * Represents a bank account type with a name and description.
 */
trait BankAccountType:
  def nameType: String
  def feePerOperation: Money
  def interestSavingJar: Double


object BankAccountType:

  def apply(nameType: String, feePerOperation: Money, interestSavingJar: Double): BankAccountType = BankAccountTypeImpl(nameType, feePerOperation, interestSavingJar)

  private case class BankAccountTypeImpl(override val nameType: String,
                                         override val feePerOperation: Money,
                                         override val interestSavingJar: Double) extends BankAccountType



