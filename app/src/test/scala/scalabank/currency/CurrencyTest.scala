package scalabank.currency

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scala.concurrent.*
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class CurrencyTest extends AnyFlatSpec:
  "Currency" should "return the correct code and symbol" in:
    val currency = Currency("USD", "$")
    currency.code should be("USD")
    currency.symbol should be("$")

  it should "create a currency instance with the given code and symbol" in:
    val currency = Currency("EUR", "€")
    currency.code should be("EUR")
    currency.symbol should be("€")

  it should "return the correct exchange rate from a real API call" in:
    val provider = ExchangeRateProvider()
    val rateFuture: Future[BigDecimal] = provider.getExchangeRate("USD", "EUR")
    val rate: BigDecimal = Await.result(rateFuture, 10.seconds)
    rate should be > BigDecimal(0)

  it should "apply fees correctly" in:
    val converter = CurrencyConverter("dummyApiKey")
    val amount = BigDecimal(100)
    val feeApplied = converter.applyFee(amount, 0.5)
    feeApplied should be(BigDecimal(99.50))

  "CurrencyConverter" should "convert an amount from one currency to another" in:
    val converter = CurrencyConverter("dummyApiKey")
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = BigDecimal(100)
    val futureResult: Future[BigDecimal] = converter.convert(amount, fromCurrency, toCurrency)
    val newAmount: BigDecimal = Await.result(futureResult, 5.seconds)
    assert(newAmount > BigDecimal(0))





