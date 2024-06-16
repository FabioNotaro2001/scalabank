package scalabank.loan

import scala.annotation.targetName

case class InterestRate(val interestValue: Double):
  assert(interestValue > 0)

extension (base: Double)
  @targetName("Add")
  def +(other: InterestRate): Double = base + other.interestValue

  @targetName("Subtraction")
  def -(other: InterestRate): Double = base - other.interestValue

  @targetName("Multiply")
  def *(other: InterestRate): Double = base * other.interestValue

  @targetName("Divide")
  def /(other: InterestRate): Double = base / other.interestValue

extension (base: InterestRate)
  @targetName("AddInterestRate")
  def +(other: InterestRate): InterestRate = InterestRate(base.interestValue + other.interestValue)

  @targetName("SubtractionInterestRate")
  def -(other: InterestRate): InterestRate = InterestRate(base.interestValue - other.interestValue)

  @targetName("MultiplyInterestRate")
  def *(other: InterestRate): InterestRate = InterestRate(base.interestValue * other.interestValue)

  @targetName("DivideInterestRate")
  def /(other: InterestRate): InterestRate = InterestRate(base.interestValue / other.interestValue)
