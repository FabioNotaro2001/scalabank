package scalabank.currency

import scala.annotation.targetName

/**
 * Object that provides methods for creating and manipulating Money instances.
 */
object Money:
  /**
   * Opaque type representing a monetary value.
   */
  opaque type Money = BigDecimal

  /**
   * Creates a Money instance from a BigDecimal amount.
   *
   * @param amount The amount to be represented as Money.
   * @return A Money instance representing the specified amount.
   * @throws IllegalArgumentException if the amount is negative.
   */
  def apply(amount: BigDecimal): Money =
    require(amount >= 0, "Amount must be non-negative")
    amount

  /**
   * Creates a Money instance from a Double amount.
   *
   * @param amount The amount to be represented as Money.
   * @return A Money instance representing the specified amount.
   * @throws IllegalArgumentException if the amount is negative.
   */
  def apply(amount: Double): Money = apply(BigDecimal(amount))

  /**
   * Extractor method to retrieve the BigDecimal value from a Money instance.
   *
   * @param money The Money instance to be extracted.
   * @return An Option containing the BigDecimal value of the Money instance.
   */
  def unapply(money: Money): Option[BigDecimal] = Some(money)

  extension (money: Money)
    /**
     * Adds two Money instances.
     *
     * @param other The Money instance to add.
     * @return A new Money instance representing the sum.
     */
    @targetName("Add")
    def +(other: Money): Money = money + other

    /**
     * Subtracts one Money instance from another.
     *
     * @param other The Money instance to subtract.
     * @return A new Money instance representing the difference.
     */
    @targetName("Subtraction")
    def -(other: Money): Money = money - other

    /**
     * Compares if one Money instance is greater than another.
     *
     * @param other The Money instance to compare.
     * @return true if this Money instance is greater than the other, false otherwise.
     */
    @targetName("Grater")
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
    @targetName("GraterOrEqual")
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
    @targetName("Division")
    def /(factor: BigDecimal): Money = money / factor

    /**
     * Formats the Money instance as a string with a dollar sign and two decimal places.
     *
     * @return The formatted string representation of the Money instance.
     */
    def format: String = f"$$${money}%.2f"
