package scalabank.Operations

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.bankAccount.{BankAccount, Deposit, StateBankAccount, Withdraw}
import scalabank.bank.BankAccountType
import scalabank.currency.{Currency, FeeManager, MoneyADT}
import scalabank.currency.MoneyADT.toMoney
import scalabank.entities.Customer

@RunWith(classOf[JUnitRunner])
class BankAccountOperationsTest extends AnyFlatSpec with Matchers:
  val customer: Customer = Customer("CUS12345L67T890M", "John", "Doe", 1980)
  val bankAccountType: BankAccountType = BankAccountType("Checking", 0.01.toMoney)
  val currency: Currency = Currency("EUR", "€")
  val initialBalance: MoneyADT.Money = 1000.toMoney

  "BankAccount" should "allow deposits and update the balance correctly" in:
    val account = BankAccount(1, customer, initialBalance, currency, StateBankAccount.Active, bankAccountType)
    account.deposit(500.toMoney)
    account.balance shouldEqual 1500.toMoney
    account.movements.size shouldBe 1
    account.movements.head shouldBe a[Deposit]
    account.movements.head.value shouldEqual 500.toMoney


  it should "allow withdrawals and update the balance correctly with fee applied" in:
    val account = BankAccount(2, customer, initialBalance, currency, StateBankAccount.Active, bankAccountType)
    val amount = 100.toMoney
    account.withdraw(amount)
    val expectedBalance = 1000.toMoney - FeeManager.calculateAmountWithFee(amount, bankAccountType.feeXOperation)
    account.balance shouldEqual expectedBalance
    account.movements.size shouldBe 1
    account.movements.head shouldBe a[Withdraw]
    account.movements.head.value shouldEqual 100.toMoney


  it should "throw an error when trying to withdraw more than the balance including the fee" in:
    val account = BankAccount(3, customer, initialBalance, currency, StateBankAccount.Active, bankAccountType)
    val largeWithdrawAmount = 2000.toMoney
    assertThrows[IllegalArgumentException]:
      account.withdraw(largeWithdrawAmount)
    account.balance shouldEqual initialBalance
    account.movements shouldBe empty


  it should "record multiple movements correctly" in:
    val account = BankAccount(4, customer, initialBalance, currency, StateBankAccount.Active, bankAccountType)
    account.deposit(200.toMoney)
    val amount = 100.toMoney
    account.withdraw(amount)
    val amount2 = 50.toMoney
    account.withdraw(amount2)
    val fee = bankAccountType.feeXOperation 
    val expectedBalance = 1000.toMoney + 200.toMoney - FeeManager.calculateAmountWithFee(amount, fee) - FeeManager.calculateAmountWithFee(amount2, fee)
    account.balance shouldEqual expectedBalance
    account.movements.size shouldBe 3
    account.movements.head shouldBe a[Deposit]
    account.movements.head.value shouldEqual 200.toMoney
    account.movements(1) shouldBe a[Withdraw]
    account.movements(1).value shouldEqual 100.toMoney
    account.movements(2) shouldBe a[Withdraw]
    account.movements(2).value shouldEqual 50.toMoney

