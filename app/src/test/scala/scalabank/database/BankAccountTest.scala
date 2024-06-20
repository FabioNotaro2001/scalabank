package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.{BankAccount, BaseBankAccount, StateBankAccount, SuperBankAccount}
import scalabank.currency.Currency
import scalabank.currency.MoneyADT.*
import scalabank.database.bank.BankAccountTable

import java.sql.{Connection, DriverManager}

@RunWith(classOf[JUnitRunner])
class BankAccountTest extends AnyFlatSpec with Matchers:
  private val connection: Connection = DriverManager.getConnection("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1")
  private val bankAccountTable = new BankAccountTable(connection)

  "BankAccountTable" should "insert and retrieve a bank account correctly" in:
    val bankAccount = BaseBankAccount(3, 1500.toMoney, Currency("USD", "$"), StateBankAccount.Active)
    bankAccountTable.insert(bankAccount)
    val retrievedBankAccount = bankAccountTable.findById(3)
    retrievedBankAccount shouldBe Some(bankAccount)

  it should "update a bank account correctly" in:
    val bankAccount = BaseBankAccount(4, 2000.toMoney, Currency("USD", "$"), StateBankAccount.Active)
    bankAccountTable.insert(bankAccount)
    val updatedBankAccount = BaseBankAccount(4, 2500.toMoney, Currency("USD", "$"), StateBankAccount.Closed)
    bankAccountTable.update(updatedBankAccount)
    val retrievedBankAccount = bankAccountTable.findById(4)
    retrievedBankAccount shouldBe Some(updatedBankAccount)

  it should "delete a bank account correctly" in:
    val bankAccount = SuperBankAccount(5, 3000.toMoney, Currency("EUR", "€"), StateBankAccount.Active)
    bankAccountTable.insert(bankAccount)
    bankAccountTable.delete(5)
    val retrievedBankAccount = bankAccountTable.findById(5)
    retrievedBankAccount shouldBe None

  it should "find all bank accounts correctly" in:
    val bankAccounts = bankAccountTable.findAll()
    bankAccounts should not be empty

