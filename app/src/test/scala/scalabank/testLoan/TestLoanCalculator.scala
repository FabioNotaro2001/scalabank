package scalabank.testLoan

import org.scalatest.matchers.should.Matchers.*
import scalabank.loan.*
import scalabank.entities.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.currency.MoneyADT.{Money, toMoney}

@RunWith(classOf[JUnitRunner])
class TestLoanCalculator extends AnyFlatSpec:
  val loanCalculator: LoanCalculator = LoanCalculator()
  val customer: Customer = Customer("GZZGLC65B22D705Y", "Gianluca", "Aguzzi", 1980)
  val requiredAmount: Money = 100000.toMoney
  val numberOfPayments = 120
  val loanCalculated: Loan = loanCalculator.calculateLoan(customer, requiredAmount, numberOfPayments)
  val interestManager: InterestManager = InterestManager()

  "The LoanCalculator" should "check if numberOfPayments is greater than 0" in:
    a [AssertionError] should be thrownBy loanCalculator.calculateLoan(customer, requiredAmount, 0)

  "The LoanCalculator" should "give the possibility to get a loan" in:
    loanCalculated shouldBe a [Loan]

  "The customer of the loan calculated" should "be the same customer who requested the loan calculation" in:
    loanCalculated.customer shouldEqual customer

  "The required amount of the loan calculated" should "be the same as the required amount requested" in:
    loanCalculated.requiredAmount shouldEqual requiredAmount

  "The number of payments of the loan calculated" should "be the same as the number of payments requested" in:
    loanCalculated.numberOfPayments shouldEqual numberOfPayments

  "The interest rate of the loan calculated" should "be correctly computed according the InterestManager" in:
    loanCalculated.interestRate shouldEqual interestManager.findAppropriateInterestForCustomer(customer)

  "The total amount of the loan calculated" should "be correcly computed from the requiredAmount and the interests" in:
    loanCalculated.totalAmount shouldEqual requiredAmount * (1.0 + interestManager.findAppropriateInterestForCustomer(customer))

  "The amount of single payment of the loan calculated" should "be correcly computed" in:
    val totalAmount = requiredAmount * (1.0 + interestManager.findAppropriateInterestForCustomer(customer))
    loanCalculated.amountOfSinglePayment shouldEqual totalAmount / numberOfPayments