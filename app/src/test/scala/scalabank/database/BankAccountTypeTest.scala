package scalabank.database

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.currency.MoneyADT.*
import scalabank.bank.BankAccountType

@RunWith(classOf[JUnitRunner])
class BankAccountTypeTest extends AnyFlatSpec:
  private val database = Database("jdbc:h2:mem:testBankAccountType;DB_CLOSE_DELAY=-1")

  import database.*

  "The BankAccountTypeTable" should "give the possibility to insert and find a bank account type" in :
    val bankAccountType = BankAccountType("student", 0.50.toMoney, 0.toMoney, 0.50.toMoney, 0.03)
    bankAccountTypeTable.insert(bankAccountType)
    val retrievedBankAccountType = bankAccountTypeTable.findById("student")
    retrievedBankAccountType should be(defined)
    retrievedBankAccountType.get shouldEqual bankAccountType

  it should "give the possibility to update a bank account type" in :
    val updatedBankAccountType = BankAccountType("student", 0.60.toMoney, 0.toMoney, 0.60.toMoney, 0.04)
    bankAccountTypeTable.update(updatedBankAccountType)
    val retrievedBankAccountType = bankAccountTypeTable.findById("student")
    retrievedBankAccountType should be(defined)
    retrievedBankAccountType.get.feeWithdraw shouldEqual 0.60.toMoney
    retrievedBankAccountType.get.feeDeposit shouldEqual 0.toMoney
    retrievedBankAccountType.get.feeMoneyTransfert shouldEqual 0.60.toMoney
    retrievedBankAccountType.get.interestSavingJar shouldEqual 0.04

  it should "give the possibility to delete a bank account type" in :
    bankAccountTypeTable.delete("student")
    val retrievedBankAccountType = bankAccountTypeTable.findById("student")
    retrievedBankAccountType should be(empty)

  it should "give the possibility to find all bank account types" in :
    val bankAccountType1 = BankAccountType("type1", 1.00.toMoney, 0.toMoney, 1.00.toMoney, 0.04)
    val bankAccountType2 = BankAccountType("type2", 0.75.toMoney, 0.toMoney, 0.75.toMoney, 0.05)
    bankAccountTypeTable.insert(bankAccountType1)
    bankAccountTypeTable.insert(bankAccountType2)
    val allBankAccountTypes = bankAccountTypeTable.findAll()
    allBankAccountTypes should contain allOf(bankAccountType1, bankAccountType2)


