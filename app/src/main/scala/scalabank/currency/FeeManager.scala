package scalabank.currency

import scalabank.currency.MoneyADT.Money

object FeeManager:
  /**
   * Applies a fee to the amount.
   *
   * @param amount        The amount to apply the fee.
   * @param feePercentage The fee percentage.
   * @return The amount after the fee is applied.
   */
  def applyPercentualFee(amount: Money, feePercentage: BigDecimal): Money =
    amount - (amount * feePercentage / 100)

  /**
   * Applies a fee to the amount.
   *
   * @param amount The amount to apply the fee.
   * @param fee    The fee.
   * @return The amount after the fee is applied.
   */
  def applyFee(amount: Money, fee: Money): Money = amount - fee

  /**
   * Add a fee to the amount.
   *
   * @param amount The amount.
   * @param fee    The fee.
   * @return The amount after the add of the fee.
   */
  def calculateAmountWithFee(amount: Money, fee: Money): Money = amount + fee

  /**
   * Add percentual fee to the amount.
   *
   * @param amount The amount.
   * @param feePercentage    The percentual fee.
   * @return The amount after the add of the fee.
   */
  def calculateAmountWithPercentualFee(amount: Money, feePercentage: BigDecimal): Money =
    amount + (amount * feePercentage / 100)

