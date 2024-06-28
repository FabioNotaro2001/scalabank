package scalabank.loan

import scalabank.currency.MoneyADT.Money
import scalabank.entities.*
import scalabank.loan.*
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

/**
 * Provides the functionality to calculate loans based on customer information and loan requirements.
 */
trait LoanCalculator:
  /**
   * Calculates a loan for a customer.
   *
   * @param customer the customer requesting the loan simulation.
   * @param requiredAmount the amount of money requested for the loan  simulation.
   * @param numberOfPayments the number of payments in which the loan will be repaid.
   * @return a Loan representing the calculated loan details.
   * @throws AssertionError if numberOfPayments is not greater than 0.
   */
  def calculateLoan(customer: Customer, requiredAmount: Money, numberOfPayments: Int): Loan

/**
 * Component trait for the LoanCalculator that includes a dependency on a logger.
 */
trait LoanCalculatorComponent:
  loggerDependency: LoggerDependency =>
  /**
   * Implementation of LoanCalculator that logs the loan calculation process.
   */
  case class LoanCalculatorImpl() extends LoanCalculator:
    private val interestManager = InterestManager()

    /**
     * Calculates and logs a loan for a customer.
     *
     * @param customer the customer requesting the loan.
     * @param requiredAmount the amount of money requested for the loan.
     * @param numberOfPayments the number of payments in which the loan will be repaid.
     * @return a `Loan` representing the calculated loan details.
     * @throws AssertionError if `numberOfPayments` is not greater than 0.
     */
    override def calculateLoan(customer: Customer, requiredAmount: Money, numberOfPayments: Int): Loan =
      assert(numberOfPayments > 0)
      val loanComputed = Loan(customer, requiredAmount, numberOfPayments, interestManager.findAppropriateInterestForCustomer(customer))
      loggerDependency.logger.log(logger.getPrefixFormatter().getLoanSimulationPrefix + loanComputed)
      loanComputed

/**
 * Singleton object that provides a LoanCalculator implementation and satisfies the LoggerDependency.
 */
object LoanCalculator extends LoggerDependency with LoanCalculatorComponent:
  override val logger: Logger = LoggerImpl()

  /**
   * Factory method to create a new LoanCalculator instance.
   *
   * @return a new instance of LoanCalculator.
   */
  def apply(): LoanCalculator = LoanCalculatorImpl()
