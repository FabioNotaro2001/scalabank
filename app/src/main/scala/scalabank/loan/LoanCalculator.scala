package scalabank.loan

import scalabank.currency.MoneyADT.Money
import scalabank.entities.*
import scalabank.loan.*

// TODO: Aggiungere logger a questo file e aggiungere un nuovo prefisso [LOAN_SIMULATION]
trait LoanCalculator:
  def calculateLoan(customer: Customer, requiredAmount: Money, numberOfPayments: Int): Loan

object LoanCalculator:
  def apply(): LoanCalculator = LoanCalculatorImpl()
  private class LoanCalculatorImpl() extends LoanCalculator:
    private val interestManager = InterestManager()
    override def calculateLoan(customer: Customer, requiredAmount: Money, numberOfPayments: Int) =
      assert(numberOfPayments > 0) 
      Loan(customer, requiredAmount, numberOfPayments, interestManager.findAppropriateInterestForCustomer(customer))