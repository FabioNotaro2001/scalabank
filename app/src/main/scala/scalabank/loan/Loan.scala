package scalabank.loan

import scalabank.currency.MoneyADT.Money
import scalabank.entities.Customer
import scalabank.loan.*

/**
 * Represents a loan agreement between a bank and a customer.
 */
trait Loan:
  /**
   * The customer who has requested the loan.
   *
   * @return the customer associated with this loan simulation.
   */
  def customer: Customer

  /**
   * The amount of money desired by the customer.
   *
   * @return the principal amount of the loan simulation.
   */
  def requiredAmount: Money

  /**
   * The number of payments to be made to repay the loan wished by the customer.
   *
   * @return the total number of payments.
   */
  def numberOfPayments: Int

  /**
   * The amount of each payment.
   *
   * @return the monetary value of a single payment.
   */
  def amountOfSinglePayment: Money

  /**
   * The interest rate applied to the loan.
   *
   * @return the interest rate of the loan.
   */
  def interestRate: InterestRate

  /**
   * The total amount to be repaid, including interest.
   *
   * @return the total repayment amount.
   */
  def totalAmount: Money

/**
 * Companion object for the Loan trait.
 */
object Loan:
  /**
   * Factory method to create a new `Loan` instance.
   *
   * @param client the customer that requested the loan simulation.
   * @param requiredAmount the principal amount of money request dy the customer.
   * @param numberOfMonthlyPayments the number of monthly payments to be made.
   * @param interestRate the interest rate for the loan.
   * @return a new Loan instance.
   */
  def apply(client: Customer, requiredAmount: Money, numberOfMonthlyPayments: Int, interestRate: InterestRate): Loan =
    LoanImpl(client, requiredAmount, numberOfMonthlyPayments, interestRate)

  /**
   * Private implementation of the Loan trait.
   *
   * @param customer the customer associated with the loan simulation.
   * @param requiredAmount the principal amount of the loan.
   * @param numberOfPayments the total number of payments.
   * @param interestRate the interest rate of the loan.
   */
  private case class LoanImpl(override val customer: Customer, override val requiredAmount: Money,
                              override val numberOfPayments: Int, override val interestRate: InterestRate) extends Loan:
    /**
     * Calculates the total amount to be repaid, including interest.
     *
     * @return the total repayment amount.
     */
    override def totalAmount: Money =  requiredAmount * (1.0 + interestRate)

    /**
     * Calculates the amount of each payment.
     *
     * @return the monetary value of a single payment.
     */
    override def amountOfSinglePayment: Money = totalAmount / numberOfPayments