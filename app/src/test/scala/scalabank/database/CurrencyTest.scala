package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.currency.Currency

@RunWith(classOf[JUnitRunner])
class CurrencyTest extends AnyFlatSpec with Matchers:

  private val database = Database("jdbc:h2:mem:testCurrency;DB_CLOSE_DELAY=-1")
  import database.*

  "CurrencyTable" should "insert and retrieve a currency correctly" in:
    val currency: Currency = Currency("USC", "$")
    currencyTable.insert(currency)
    val retrievedCurrency = currencyTable.findById(currency.code)
    retrievedCurrency shouldBe Some(currency)

  it should "update a currency correctly" in:
    val updatedCurrency = Currency("USC", "US$")
    currencyTable.update(updatedCurrency)
    val retrievedCurrency = currencyTable.findById(updatedCurrency.code)
    retrievedCurrency shouldBe Some(updatedCurrency)

  it should "delete a currency correctly" in:
    val currencyCode = "USC"
    currencyTable.delete(currencyCode)
    val retrievedCurrency = currencyTable.findById(currencyCode)
    retrievedCurrency shouldBe None

  it should "find all currencies correctly" in:
    val currency1 = Currency("USC", "$")
    val currency2 = Currency("EUD", "€")
    val currency3 = Currency("JPJ", "¥")
    currencyTable.delete("USC")
    currencyTable.delete("EUD")
    currencyTable.delete("JPJ")
    currencyTable.insert(currency1)
    currencyTable.insert(currency2)
    currencyTable.insert(currency3)
    val retrievedCurrencies = currencyTable.findAll()
    retrievedCurrencies should contain allOf (currency1, currency2, currency3)

