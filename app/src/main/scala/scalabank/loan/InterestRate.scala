package scalabank.loan

import scala.annotation.targetName

/**
 * Represents an interest rate.
 *
 * @param interestValue the value of the interest rate, must be positive.
 * @throws AssertionError if `interestValue` is not greater than 0.
 */
case class InterestRate(interestValue: Double):
  assert(interestValue > 0, "Interest rate must be greater than 0")

  /**
   * Returns a string representation of the interest rate in percentage format.
   *
   * @return the interest rate as a percentage string.
   */
  override def toString: String = s"${interestValue * 100}%"

/**
 * Provides extension methods for Double to perform arithmetic operations with InterestRate.
 */
extension (base: Double)
  /**
   * Adds a Double and an InterestRate.
   *
   * @param other the InterestRate to add.
   * @return the sum as a Double.
   */
  @targetName("Add")
  def +(other: InterestRate): Double = base + other.interestValue

  /**
   * Subtracts an InterestRate from a Double.
   *
   * @param other the InterestRate to subtract.
   * @return the difference as a Double.
   */
  @targetName("Subtraction")
  def -(other: InterestRate): Double = base - other.interestValue

  /**
   * Multiplies a Double by an InterestRate.
   *
   * @param other the InterestRate to multiply by.
   * @return the product as a Double.
   */
  @targetName("Multiply")
  def *(other: InterestRate): Double = base * other.interestValue

  /**
   * Divides a Double by an InterestRate.
   *
   * @param other the InterestRate to divide by.
   * @return the quotient as a Double.
   */
  @targetName("Divide")
  def /(other: InterestRate): Double = base / other.interestValue

/**
 * Provides extension methods for InterestRate to perform arithmetic operations with another InterestRate.
 */
extension (base: InterestRate)
  /**
   * Adds two InterestRate instances.
   *
   * @param other the InterestRate to add.
   * @return the sum as an InterestRate.
   */
  @targetName("AddInterestRate")
  def +(other: InterestRate): InterestRate = InterestRate(base.interestValue + other.interestValue)

  /**
   * Subtracts one InterestRate from another.
   *
   * @param other the InterestRate to subtract.
   * @return the difference as an InterestRate.
   */
  @targetName("SubtractionInterestRate")
  def -(other: InterestRate): InterestRate = InterestRate(base.interestValue - other.interestValue)

  /**
   * Multiplies two InterestRate instances.
   *
   * @param other the InterestRate to multiply by.
   * @return the product as an InterestRate.
   */
  @targetName("MultiplyInterestRate")
  def *(other: InterestRate): InterestRate = InterestRate(base.interestValue * other.interestValue)

  /**
   * Divides one InterestRate by another.
   *
   * @param other the InterestRate to divide by.
   * @return the quotient as an InterestRate.
   */
  @targetName("DivideInterestRate")
  def /(other: InterestRate): InterestRate = InterestRate(base.interestValue / other.interestValue)
