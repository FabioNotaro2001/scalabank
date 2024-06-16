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
      assert(numberOfPayments > 0)  // TODO: dire a Bedo di aggiungere conversione opposta, cio+ da Money ad Int e qui aggiungere condizione che anche required amount dev'essere > 0. Nel caso aggiungere test corrispondente.
      Loan(customer, requiredAmount, numberOfPayments, interestManager.findAppropriateInterestForCustomer(customer))