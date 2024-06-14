package scalabank.currency

import scala.annotation.targetName

opaque type Money = BigDecimal

object Money:
  def apply(amount: BigDecimal): Money = amount
  def apply(amount: Int): Money = BigDecimal(amount)
  def apply(amount: Double): Money = BigDecimal(amount)
  def unapply(money: Money): Option[BigDecimal] = Some(money)

  extension (money: Money)
    def toBigDecimal: BigDecimal = money
    @targetName("Add")
    def +(other: Money): Money = money + other
    @targetName("Subtraction")
    def -(other: Money): Money = money - other
    @targetName("Grater")
    def >(other: Money): Boolean = money > other
    @targetName("Lower")
    def <(other: Money): Boolean = money < other
    @targetName("GraterOrEqual")
    def >=(other: Money): Boolean = money >= other
    @targetName("LowerOrEqual")
    def <=(other: Money): Boolean = money <= other
    @targetName("Multiply")
    def *(factor: BigDecimal): Money = money * factor
    @targetName("Division")
    def /(factor: BigDecimal): Money = money / factor
    def format: String = f"$$${money}%.2f"
