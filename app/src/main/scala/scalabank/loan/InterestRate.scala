package scalabank.loan

import scala.annotation.targetName

case class InterestRate(val interestValue: Double):
  assert(interestValue > 0)

extension(base: Double)
  @targetName("Add")
  def +(interestToAdd: InterestRate): Double = base + interestToAdd.interestValue

  @targetName("Subtraction")
  def -(interestToRemove: InterestRate): Double = base - interestToRemove.interestValue

  @targetName("Multiply")
  def *(interestToMultiply: InterestRate): Double = base * interestToMultiply.interestValue

  @targetName("Divide")
  def /(interestToDivide: InterestRate): Double = base / interestToDivide.interestValue

