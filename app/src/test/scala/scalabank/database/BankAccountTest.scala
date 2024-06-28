package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.bank.BankAccountType
import scalabank.bankAccount.{BankAccount, StateBankAccount}
import scalabank.currency.Currency
import scalabank.currency.MoneyADT.*
import scalabank.database.bank.BankAccountTable
import scalabank.database.customer.CustomerTable

import java.sql.{Connection, DriverManager}

@RunWith(classOf[JUnitRunner])
class BankAccountTest extends AnyFlatSpec with Matchers:
  private val database = Database("jdbc:h2:mem:bankAccount;DB_CLOSE_DELAY=-1")
  import database.*

  "BankAccountTable" should "insert and retrieve a bank account correctly" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", 1.toMoney, 0.toMoney, 1.toMoney, 0.04)
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("USD", "$")
    val state = StateBankAccount.Active
    val bankAccount = BankAccount(10, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount)
    val retrievedBankAccount = bankAccountTable.findById(10)
    retrievedBankAccount shouldBe Some(bankAccount)

  it should "update a bank account correctly" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", 1.toMoney, 0.toMoney, 1.toMoney, 0.04)
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("USD", "$")
    val state = StateBankAccount.Active
    val bankAccount = BankAccount(20, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount)
    val updatedBalance = BigDecimal(2000).toMoney
    val updatedBankAccount = BankAccount(2, customer, updatedBalance, currency, state, accountType)
    bankAccountTable.update(updatedBankAccount)
    val retrievedBankAccount = bankAccountTable.findById(2)
    retrievedBankAccount shouldBe Some(updatedBankAccount)

  it should "delete a bank account correctly" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", 0.01.toMoney, 0.toMoney, 0.01.toMoney, 0.5)
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("USD", "$")
    val state = StateBankAccount.Active
    val bankAccount = BankAccount(30, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount)
    bankAccountTable.delete(3)
    val retrievedBankAccount = bankAccountTable.findById(3)
    retrievedBankAccount shouldBe None

  it should "find all bank accounts" in:
    val customers = customerTable.findAll()
    val customer = customers.head
    val accountType = BankAccountType("Checking", 1.toMoney, 0.toMoney, 1.toMoney, 0.04)
    val balance = BigDecimal(1000).toMoney
    val currency = Currency("USD", "$")
    val state = StateBankAccount.Active
    val bankAccount1 = BankAccount(40, customer, balance, currency, state, accountType)
    val bankAccount2 = BankAccount(50, customer, balance, currency, state, accountType)
    bankAccountTable.insert(bankAccount1)
    bankAccountTable.insert(bankAccount2)
    val allBankAccounts = bankAccountTable.findAll()
    allBankAccounts should contain allOf(bankAccount1, bankAccount2)
  


