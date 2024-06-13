package scalabank.currency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Trait representing a currency converter with conversion and fee application methods.
 */
trait CurrencyConverter:
  /**
   * Converts an amount from one currency to another.
   *
   * @param amount The amount to convert.
   * @param from   The source currency.
   * @param to     The target currency.
   * @return The converted amount.
   */
  def convert(amount: BigDecimal, from: Currency, to: Currency): Future[BigDecimal]

  /**
   * Applies a fee to the amount.
   *
   * @param amount        The amount to apply the fee.
   * @param feePercentage The fee percentage.
   * @return The amount after the fee is applied.
   */
  def applyFee(amount: BigDecimal, feePercentage: BigDecimal): BigDecimal =
    amount - (amount * feePercentage / 100)


object CurrencyConverter:
  def apply(apiKey: String): CurrencyConverter = OnlineCurrencyConverter()

  private case class OnlineCurrencyConverter() extends CurrencyConverter:
    private val exchangeRateProvider = ExchangeRateProvider()

    override def convert(amount: BigDecimal, from: Currency, to: Currency): Future[BigDecimal] =
      exchangeRateProvider.getExchangeRate(from.code, to.code).map(rate => amount * rate)

