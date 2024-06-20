package scalabank.testLoan

import org.scalatest.matchers.should.Matchers.*
import scalabank.loan.{InterestRate, *}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestInterestRate extends AnyFlatSpec:
  val rateOfTenPercent: InterestRate = InterestRate(0.10)
  val rateOfFivePercent: InterestRate = InterestRate(0.05)
  val doubleValOfHundred: Double = 100.0
  val tolerance = 0.01

  "An InterestRate" should "be positive" in:
    a [AssertionError] should be thrownBy InterestRate(-0.05)

  "An InterestRate" should "initialize correctly with positive value" in:
    rateOfTenPercent.interestValue shouldEqual 0.10

  "A Double" should "be added correctly to an InterestRate" in:
    val result = doubleValOfHundred + rateOfTenPercent
    result shouldBe 100.10

  "A Double" should "be subtracted correctly to an InterestRate" in:
    val result = doubleValOfHundred - rateOfTenPercent
    result shouldBe 99.90

  "A Double" should "be multiplied correctly to an InterestRate" in:
    val result = doubleValOfHundred * rateOfTenPercent
    result shouldBe 10.0

  "A Double" should "be divided correctly to an InterestRate" in:
    val result = doubleValOfHundred / rateOfTenPercent
    result shouldBe 1000.0

  "The sum between two InterestRates" should "be computed correctly" in:
    val result = rateOfTenPercent + rateOfFivePercent
    result.interestValue shouldBe 0.15 +- tolerance

  "The subtraction between two InterestRates" should "be computed correctly" in:
    val result = rateOfTenPercent - rateOfFivePercent
    result.interestValue shouldBe 0.05 +- tolerance

  "The multiplication between two InterestRates" should "be computed correctly" in:
    val result = rateOfTenPercent * rateOfFivePercent
    result.interestValue shouldBe 0.005 +- tolerance

  "The division between two InterestRates" should "be computed correctly" in:
    val result = rateOfTenPercent / rateOfFivePercent
    result.interestValue shouldBe 2.0 +- tolerance
