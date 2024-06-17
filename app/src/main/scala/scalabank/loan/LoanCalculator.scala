package scalabank.loan

import scalabank.currency.MoneyADT.Money
import scalabank.entities.*
import scalabank.loan.*
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

// TODO: Aggiungere logger a questo file e aggiungere un nuovo prefisso [LOAN_SIMULATION]
trait LoanCalculator:
  def calculateLoan(customer: Customer, requiredAmount: Money, numberOfPayments: Int): Loan

trait LoanCalculatorComponent:
  loggerDependency: LoggerDependency =>
  case class LoanCalculatorImpl() extends LoanCalculator:
    private val interestManager = InterestManager()
    override def calculateLoan(customer: Customer, requiredAmount: Money, numberOfPayments: Int) =
      assert(numberOfPayments > 0)
      val loanComputed = Loan(customer, requiredAmount, numberOfPayments, interestManager.findAppropriateInterestForCustomer(customer))
      loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + loanComputed)
      loanComputed

object LoanCalculator extends LoggerDependency with LoanCalculatorComponent:
  override val logger: Logger = LoggerImpl()
  def apply(): LoanCalculator = LoanCalculatorImpl()