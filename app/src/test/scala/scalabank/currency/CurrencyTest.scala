package scalabank.currency

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scala.concurrent.*
import scala.concurrent.duration._
import scalabank.currency.MoneyOperation.*

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
    val amount = MoneyOperation(100)
    val feeApplied = converter.applyFee(amount, 0.5)
    feeApplied should be(BigDecimal(99.50))

  "CurrencyConverter" should "convert an amount from one currency to another" in:
    val converter = CurrencyConverter()
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = MoneyOperation(100)
    val result: Money = converter.convert(amount, fromCurrency, toCurrency)
    assert(result > MoneyOperation(0))

  "CurrencyConverterWithFee" should "convert an amount from one currency to another with default fee" in:
    val converter = CurrencyConverter()
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = MoneyOperation(100)
    import CurrencyConverter.feeRateDefault
    val result: Money = converter.convertWithFee(amount, fromCurrency, toCurrency)
    assert(result > MoneyOperation(0))

  "CurrencyConverterWithFee" should "convert an amount from one currency to another with specified fee" in:
    val converter = CurrencyConverter()
    val fromCurrency = Currency("USD", "$")
    val toCurrency = Currency("EUR", "€")
    val amount = MoneyOperation(100)
    val result: Money = converter.convertWithFee(amount, fromCurrency, toCurrency)(using 5)
    assert(result > MoneyOperation(0))

  "Money" should "create an instance from BigDecimal" in :
    val amount = MoneyOperation(BigDecimal(100))
    amount shouldEqual BigDecimal(100)

  it should "not include negative numbers" in:
    intercept[IllegalArgumentException] :
      MoneyOperation(-100)

  it should "create an instance from Int" in :
    val amount = MoneyOperation(100)
    amount shouldEqual BigDecimal(100)

  it should "create an instance from Double" in :
    val amount = MoneyOperation(100.5)
    amount shouldEqual BigDecimal(100.5)

  it should "correctly add two amounts" in :
    val amount1 = MoneyOperation(100)
    val amount2 = MoneyOperation(50)
    amount1 + amount2 shouldEqual BigDecimal(150)

  it should "correctly subtract two amounts" in :
    val amount1 = MoneyOperation(100)
    val amount2 = MoneyOperation(50)
    amount1 - amount2 shouldEqual BigDecimal(50)

  it should "correctly multiply by a factor" in :
    val amount = MoneyOperation(100)
    val factor = BigDecimal(1.5)
    amount * factor shouldEqual BigDecimal(150)

  it should "correctly divide by a factor" in :
    val amount = MoneyOperation(100)
    val factor = BigDecimal(2)
    amount / factor shouldEqual BigDecimal(50)

  it should "correctly format as a string" in :
    val amount = MoneyOperation(100.1234)
    amount.format shouldEqual "$100.12"

  it should "throw an exception when dividing by zero" in :
    val amount = MoneyOperation(100)
    intercept[ArithmeticException] :
      amount / BigDecimal(0)

  it should "apply a fee correctly" in :
    val amount = MoneyOperation(100)
    val feePercentage = BigDecimal(10)
    amount - (amount * feePercentage / 100) shouldEqual BigDecimal(90)

  it should "compare two Money instances correctly" in :
    val amount1 = MoneyOperation(100)
    val amount2 = MoneyOperation(100)
    val amount3 = MoneyOperation(50)
    (amount1 == amount3) shouldBe false

  it should "handle very large values correctly" in :
    val largeAmount = MoneyOperation(BigDecimal("1000000000000000000000000000"))
    largeAmount shouldEqual BigDecimal("1000000000000000000000000000")

  it should "handle very small values correctly" in :
    val smallAmount = MoneyOperation(BigDecimal("0.0000000000000000000000001"))
    smallAmount shouldEqual BigDecimal("0.0000000000000000000000001")





