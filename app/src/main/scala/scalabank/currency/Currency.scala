package scalabank.currency

/**
 * Represents a currency with code and symbol.
 */
trait Currency:
  /**
   * The currency code.
   * @return The code of the currency.
   */
  def code: String

  /**
   * The currency symbol
   * @return The symbol of the currency.
   */
  def symbol: String


object Currency:
  def apply(code: String, symbol: String): Currency = CurrencyImpl(code, symbol)

  private case class CurrencyImpl(override val code: String,
                                  override val symbol: String) extends Currency

