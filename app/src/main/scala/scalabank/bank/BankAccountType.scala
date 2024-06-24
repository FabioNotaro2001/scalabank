package scalabank.bank

import scalabank.currency.MoneyADT.Money

/**
 * Represents a bank account type with a name and description.
 */
trait BankAccountType:
  def nameType: String
  def feePerOperation: Money


object BankAccountType:

  def apply(nameType: String, feePerOperation: Money): BankAccountType = BankAccountTypeImpl(nameType, feePerOperation)

  private case class BankAccountTypeImpl(override val nameType: String,
                                         override val feePerOperation: Money ) extends BankAccountType



