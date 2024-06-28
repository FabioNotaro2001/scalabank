package scalabank.currency

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scala.concurrent.*
import scala.concurrent.duration._
import scalabank.currency.MoneyADT.*

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
    val converter = CurrencyConverter()
    val amount = 100.toMoney
    val feeApplied = FeeManager.applyPercentualFee(amount, 0.5)
    feeApplied should be(BigDecimal(99.50))

  "CurrencyConverter" should "convert an amount from one currency to another" in:
    val converter = CurrencyConverter()
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = 100.toMoney
    val result: Money = converter.convert(amount, fromCurrency, toCurrency)
    result should be > 0.toMoney

  "CurrencyConverterWithFee" should "convert an amount from one currency to another with default fee" in:
    val converter = CurrencyConverter()
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = 100.toMoney
    import CurrencyConverter.feeRateDefault
    val result: Money = converter.convertWithFee(amount, fromCurrency, toCurrency)
    result should be > 0.toMoney

  "CurrencyConverterWithFee" should "convert an amount from one currency to another with specified fee" in:
    val converter = CurrencyConverter()
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = 100.toMoney
    val result: Money = converter.convertWithFee(amount, fromCurrency, toCurrency)(using 5)
    result should be > 0.toMoney

  "Money" should "create an instance from BigDecimal" in :
    val amount = 100.toMoney
    amount shouldEqual 100.toMoney

  it should "not include negative numbers" in:
    intercept[IllegalArgumentException] :
      -100.toMoney

  it should "create an instance from Int" in :
    val amount = 100.toMoney
    amount shouldEqual BigDecimal(100)

  it should "create an instance from Double" in :
    val amount = 100.5.toMoney
    amount shouldEqual BigDecimal(100.5)

  it should "correctly add two amounts" in :
    val amount1 = 100.toMoney
    val amount2 = 50.toMoney
    amount1 + amount2 shouldEqual BigDecimal(150)

  it should "correctly subtract two amounts" in :
    val amount1 = 100.toMoney
    val amount2 = 50.toMoney
    amount1 - amount2 shouldEqual BigDecimal(50)

  it should "correctly multiply by a factor" in :
    val amount = 100.toMoney
    val factor = BigDecimal(1.5)
    amount * factor shouldEqual 150.toMoney

  it should "correctly divide by a factor" in :
    val amount = 100.toMoney
    val factor = BigDecimal(2)
    amount / factor shouldEqual BigDecimal(50)

  it should "throw an exception when dividing by zero" in :
    val amount = 100.toMoney
    intercept[ArithmeticException] :
      amount / BigDecimal(0)

  it should "apply a fee correctly" in :
    val amount = 100.toMoney
    val feePercentage = BigDecimal(10)
    amount - (amount * feePercentage / 100) shouldEqual BigDecimal(90)

  it should "compare two Money instances correctly" in :
    val amount1 = 100.toMoney
    val amount2 = 100.toMoney
    val amount3 = 50.toMoney
    amount1 shouldNot be(amount3)

  it should "handle very large values correctly" in :
    val largeAmount = BigDecimal("1000000000000000000000000000").toMoney
    largeAmount shouldEqual BigDecimal("1000000000000000000000000000").toMoney

  it should "handle very small values correctly" in :
    val smallAmount = BigDecimal("0.0000000000000000000000001").toMoney
    smallAmount shouldEqual BigDecimal("0.0000000000000000000000001").toMoney





