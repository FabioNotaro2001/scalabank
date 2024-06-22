package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.bank.BankAccountType
import scalabank.entities.{BankAccount, BaseBankAccount, StateBankAccount, SuperBankAccount}
import scalabank.currency.Currency
import scalabank.currency.MoneyADT.*
import scalabank.database.bank.BankAccountTable
import scalabank.database.customer.CustomerTable

import java.sql.{Connection, DriverManager}

@RunWith(classOf[JUnitRunner])
class BankAccountTest extends AnyFlatSpec with Matchers:
  private val connection: Connection = DriverManager.getConnection("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1")
  private val customerTable = new CustomerTable(connection)
  private val bankAccountTable = new BankAccountTable(connection, customerTable)

  "BankAccountTable" should "insert and retrieve a bank account correctly" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", BigDecimal(0.01))
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("EUR", "€")
    val state = StateBankAccount.Active
    val bankAccount = BankAccount(1, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount)
    val retrievedBankAccount = bankAccountTable.findById(1)
    retrievedBankAccount shouldBe Some(bankAccount)

  it should "update a bank account correctly" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", BigDecimal(0.01))
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("EUR", "€")
    val state = StateBankAccount.Active
    val bankAccount = BankAccount(2, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount)
    val updatedBalance = BigDecimal(2000).toMoney
    val updatedBankAccount = BankAccount(2, customer, updatedBalance, currency, state, accountType)
    bankAccountTable.update(updatedBankAccount)
    val retrievedBankAccount = bankAccountTable.findById(2)
    retrievedBankAccount shouldBe Some(updatedBankAccount)

  it should "delete a bank account correctly" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", BigDecimal(0.01))
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("EUR", "€")
    val state = StateBankAccount.Active
    val bankAccount = BankAccount(3, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount)
    bankAccountTable.delete(3)
    val retrievedBankAccount = bankAccountTable.findById(3)
    retrievedBankAccount shouldBe None

  it should "find all bank accounts" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", BigDecimal(0.01))
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("EUR", "€")
    val state = StateBankAccount.Active
    val bankAccount1 = BankAccount(4, customer, balance, currency, state, accountType)
    val bankAccount2 = BankAccount(5, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount1)
    bankAccountTable.insert(bankAccount2)
    val allBankAccounts = bankAccountTable.findAll()
    allBankAccounts should contain allOf(bankAccount1, bankAccount2)
  


