package scalabank.bank

import scalabank.currency.MoneyADT.Money

/**
 * Represents a bank account type with a name and description.
 */
trait BankAccountType:
  /**
   * The name of the bank account type.
   * @return the name of the bank account type
   */
  def nameType: String

  /**
   * The fee for withdrawals.
   * @return the fee for withdrawals
   */
  def feeWithdraw: Money

  /**
   * The fee for deposits.
   * @return the fee for deposits
   */
  def feeDeposit: Money

  /**
   * The fee for money transfers.
   * @return the fee for money transfers
   */
  def feeMoneyTransfert: Money

  /**
   * The interest rate for the saving jar.
   * @return the interest rate for the saving jar
   */
  def interestSavingJar: Double


object BankAccountType:

  def apply(nameType: String, feeWithdraw: Money, feeDeposit: Money, feeMoneyTransfert: Money, interestSavingJar: Double): BankAccountType = BankAccountTypeImpl(nameType, feeWithdraw, feeDeposit, feeMoneyTransfert, interestSavingJar)

  private case class BankAccountTypeImpl(override val nameType: String,
                                         override val feeWithdraw: Money,
                                         override val feeDeposit: Money,
                                         override val feeMoneyTransfert: Money,
                                         override val interestSavingJar: Double) extends BankAccountType



