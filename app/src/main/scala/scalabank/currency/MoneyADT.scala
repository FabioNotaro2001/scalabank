package scalabank.currency

import scala.annotation.targetName

/**
 * Object that provides methods for creating and manipulating Money instances.
 */
object MoneyADT:
  /**
   * Opaque type representing a monetary value.
   */
  opaque type Money = BigDecimal

  /**
   * Extractor method to retrieve the BigDecimal value from a Money instance.
   *
   * @param money The Money instance to be extracted.
   * @return An Option containing the BigDecimal value of the Money instance.
   */
  def unapply(money: Money): Option[BigDecimal] = Some(money)

  given Ordering[Money] with
    override def compare(x: Money, y: Money): Int = x.compare(y)

  extension (amount: Double | Int | Float | String | BigDecimal)
    def toMoney: Money =
      amount match
        case amountAsBig: BigDecimal if amountAsBig >= BigDecimal(0) => amountAsBig
        case amountAsDouble: Double if amountAsDouble >= 0 => BigDecimal(amountAsDouble)
        case amountAsInt: Int if amountAsInt >= 0 => BigDecimal(amountAsInt)
        case amountAsFloat: Float if amountAsFloat >= 0 => BigDecimal(amountAsFloat)
        case amountAsString: String if BigDecimal(amountAsString) >= 0 => BigDecimal(amountAsString)
        case _ => throw new IllegalArgumentException("Amount must be non-negative")

  extension (money: Money)
    /**
     * Adds two Money instances.
     *
     * @param moneyToAdd The Money instance to add.
     * @return A new Money instance representing the sum.
     */
    @targetName("Add")
    def +(moneyToAdd: Money): Money = money + moneyToAdd

    /**
     * Subtracts one Money instance from another.
     *
     * @param moneyToGet The Money instance to subtract.
     * @return A new Money instance representing the difference.
     */
    @targetName("Subtraction")
    def -(moneyToGet: Money): Money = money - moneyToGet

    /**
     * Compares if one Money instance is greater than another.
     *
     * @param other The Money instance to compare.
     * @return true if this Money instance is greater than the other, false otherwise.
     */
    @targetName("Greater")
    def >(other: Money): Boolean = money > other

    /**
     * Compares if one Money instance is less than another.
     *
     * @param other The Money instance to compare.
     * @return true if this Money instance is less than the other, false otherwise.
     */
    @targetName("Lower")
    def <(other: Money): Boolean = money < other

    /**
     * Compares if one Money instance is greater than or equal to another.
     *
     * @param other The Money instance to compare.
     * @return true if this Money instance is greater than or equal to the other, false otherwise.
     */
    @targetName("GreaterOrEqual")
    def >=(other: Money): Boolean = money >= other

    /**
     * Compares if one Money instance is less than or equal to another.
     *
     * @param other The Money instance to compare.
     * @return true if this Money instance is less than or equal to the other, false otherwise.
     */
    @targetName("LowerOrEqual")
    def <=(other: Money): Boolean = money <= other

    /**
     * Multiplies the Money instance by a BigDecimal factor.
     *
     * @param factor The factor to multiply by.
     * @return A new Money instance representing the product.
     */
    @targetName("Multiply")
    def *(factor: BigDecimal): Money = money * factor

    /**
     * Divides the Money instance by a BigDecimal factor.
     *
     * @param factor The factor to divide by.
     * @return A new Money instance representing the quotient.
     */
    @targetName("Divide")
    def /(factor: BigDecimal): Money = money / factor

    /**
     * Formats the Money instance as a string with a dollar sign and two decimal places.
     *
     * @return The formatted string representation of the Money instance.
     */
    def format: String = f"${money}%.2f"

    /**
     * Converts the Money instance to a BigDecimal.
     *
     * @return The BigDecimal representation of the Money instance.
     */
    def toBigDecimal: BigDecimal = money
