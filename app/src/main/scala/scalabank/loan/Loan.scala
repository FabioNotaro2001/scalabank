package scalabank.loan

import scalabank.currency.MoneyADT.Money
import scalabank.entities.Customer
import scalabank.loan.*

trait Loan:
  def customer: Customer
  def requiredAmount: Money
  def numberOfPayments: Int
  def amountOfSinglePayment: Money
  def interestRate: InterestRate
  def totalAmount: Money

object Loan:
  def apply(client: Customer, requiredAmount: Money, numberOfMonthlyPayments: Int, interestRate: InterestRate): Loan =
    LoanImpl(client, requiredAmount, numberOfMonthlyPayments, interestRate)

  private case class LoanImpl(override val customer: Customer, override val requiredAmount: Money,
                              override val numberOfPayments: Int, override val interestRate: InterestRate) extends Loan:
    override def totalAmount: Money =  requiredAmount * (1.0 + interestRate)
    override def amountOfSinglePayment: Money = totalAmount / numberOfPayments