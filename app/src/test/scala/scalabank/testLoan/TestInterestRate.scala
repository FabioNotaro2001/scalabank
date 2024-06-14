package scalabank.testLoan

import org.scalatest.matchers.should.Matchers.*
import scalabank.loan.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestInterestRate extends AnyFlatSpec:
  val rate = InterestRate(0.05)
  val base: Double = 100.0

  "An InterestRate" should "be positive" in:
    assertThrows[AssertionError] {
      InterestRate(-0.05)
    }

  "An InterestRate" should "initialize correctly with positive value" in:
    assert(rate.interestValue == 0.05)

  "Double + InterestRate" should "add interest correctly" in:
    val interestRateToAdd = InterestRate(0.05)
    val result = base + interestRateToAdd
    assert(result == 100.05)

  "Double - InterestRate" should "subtract interest correctly" in:
    val interestRateToRemove = InterestRate(0.05)
    val result = base - interestRateToRemove
    assert(result == 99.95)

  "Double * InterestRate" should "multiply interest correctly" in:
    val interestRateToMultiply = InterestRate(0.05)
    val result = base * interestRateToMultiply
    assert(result == 5.0)

  "Double / InterestRate" should "divide interest correctly" in:
    val interestRateToDivide = InterestRate(0.05)
    val result = base / interestRateToDivide
    assert(result == 2000.0)