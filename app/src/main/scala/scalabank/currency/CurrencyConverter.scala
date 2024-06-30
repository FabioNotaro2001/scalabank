package scalabank.currency

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scalabank.currency.MoneyADT.*
import scala.concurrent.duration._

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
  def convert(amount: Money, from: Currency, to: Currency): Money

  /**
   * Converts an amount from one currency to another.
   *
   * @param amount The amount to convert.
   * @param from   The source currency.
   * @param to     The target currency.
   * @param feePercentage The fee percentage.
   * @return The converted amount.
   */
  def convertWithFee(amount: Money, from: Currency, to: Currency)(using feePercentage: BigDecimal): Money


object CurrencyConverter:
  def apply(): CurrencyConverter = OnlineCurrencyConverter()

  /**
   * Given for default fee rate.
   */
  given feeRateDefault: BigDecimal = BigDecimal(0.2)

  private case class OnlineCurrencyConverter() extends CurrencyConverter:
    private val exchangeRateProvider = ExchangeRateProvider()

    override def convert(amount: Money, from: Currency, to: Currency): Money =
      Await.result(exchangeRateProvider.getExchangeRate(from.code, to.code).map(_ => amount), 5.second)

    override def convertWithFee(amount: Money, from: Currency, to: Currency)(using feePercentage: BigDecimal): Money =
      val convertedAmount = convert(amount, from, to)
      FeeManager.applyPercentageFee(convertedAmount, feePercentage)




