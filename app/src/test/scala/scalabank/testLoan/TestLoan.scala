package scalabank.testLoan

import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.*
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.Employee
import scalabank.entities.Employee.EmployeePosition
import scalabank.entities.Customer
import scalabank.loan.Loan

@RunWith(classOf[JUnitRunner])
class TestLoan extends AnyFlatSpec with BeforeAndAfterEach:
  val customer: Customer = Customer("Mirko", "Viroli", 1980)
  val loan = Loan(customer, 12000, 12, 5.5)

  "The loan" should "have the correct client" in:
    assert(loan.customer == customer)

  "The loan" should "have the correct required amount" in:
    assert(loan.requiredAmount == 12000)

  "The loan" should "have the correct number of monthly payments" in:
    assert(loan.numberOfMonthlyPayments == 12)

  "The loan" should "have the correct amount of single payment" in:
    val expectedSinglePayment = loan.totalAmount / loan.numberOfMonthlyPayments
    assert(loan.amountOfSinglePayment == expectedSinglePayment)

  "The loan" should "have the correct interest rate" in:
    assert(loan.interestRate == 5.5)

  "The loan" should "have the correct total amount" in:
    val expectedTotalAmount = loan.requiredAmount * (1 + loan.interestRate)
    assert(loan.totalAmount == expectedTotalAmount)