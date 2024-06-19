package scalabank.testLoan

import org.scalatest.matchers.should.Matchers.*
import scalabank.loan.*
import scalabank.entities.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestInterestManager extends AnyFlatSpec:
  val interestManager = InterestManager()
  val interestProvider = InterestProvider()

  "The InterestManager" should "provide correct interest rate for a young customer" in:
    val youngCustomer = Customer("Alex", "Mazzotti", 2004)
    val interestRate = interestManager.findAppropriateInterestForCustomer(youngCustomer)
    interestRate shouldEqual interestProvider.getInterestForYoungCustomer

  "The InterestManager" should "provide correct interest rate for an old customer" in:
    val oldCustomer = Customer("Giacomo", "Bertuccioli", 1950)
    val interestRate = interestManager.findAppropriateInterestForCustomer(oldCustomer)
    interestRate shouldEqual interestProvider.getInterestForOldCustomer

  "The InterestManager" should "provide default interest rate for a normal customer" in:
    val defaultCustomer = Customer("Andrea", "Bedei", 1980)
    val interestRate = interestManager.findAppropriateInterestForCustomer(defaultCustomer)
    interestRate shouldEqual interestProvider.getDefaultInterest