package scalabank.testLoan

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.Customer
import scalabank.loan.*
import scalabank.currency.MoneyADT.*

@RunWith(classOf[JUnitRunner])
class TestLoan extends AnyFlatSpec:
  val customer = Customer("Mirko", "Viroli", 1980)
  val loan = Loan(customer, 12000.toMoney, 12, InterestRate(0.05))

  "The loan" should "have the correct client" in:
    loan.customer shouldEqual customer

  "The loan" should "have the correct required amount" in:
    loan.requiredAmount shouldEqual 12000.toMoney

  "The loan" should "have the correct number of monthly payments" in:
    loan.numberOfMonthlyPayments shouldBe 12

  "The loan" should "have the correct amount of single payment" in:
    val expectedSinglePayment = loan.totalAmount / loan.numberOfMonthlyPayments
    loan.amountOfSinglePayment shouldEqual expectedSinglePayment

  "The loan" should "have the correct interest rate" in:
    loan.interestRate shouldBe InterestRate(0.05)

  "The loan" should "have the correct total amount" in:
    val expectedTotalAmount = loan.requiredAmount * (1.0 + loan.interestRate)
    loan.totalAmount shouldEqual expectedTotalAmount