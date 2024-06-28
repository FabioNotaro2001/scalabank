package scalabank.bank

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.junit.JUnitRunner
import scalabank.bank.Bank
import scalabank.bank.Bank.{PhysicalBank, PhysicalBankInformation}
import scalabank.bankAccount.BankAccount
import scalabank.currency.MoneyADT.toMoney
import scalabank.currency.{Currency, MoneyADT}
import scalabank.entities.{Customer, SavingsJar}

@RunWith(classOf[JUnitRunner])
class SavingJarTest extends AnyFunSuite:

  val customer: Customer = Customer("JHNDOE22B705Y", "John", "Doe", 2000)
  val bank: Bank.PhysicalBank = PhysicalBank(PhysicalBankInformation("Cesena Bank", "via Roma 3", "12345678"))
  bank.addBankAccountType("Base BankAccount", 2.toMoney, 0.toMoney, 2.toMoney, 0.5)
  customer.registerBank(bank)
  val bankAccountType: BankAccountType = bank.getBankAccountTypes.head
  val currency: Currency = Currency("EUR", "€")

  test("Deposit should fail if balance of bankAccount is insufficient"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    bankAccount.balance shouldBe 0.toMoney
    assertThrows[IllegalArgumentException]:
      savingJar.deposit(200.toMoney)
    savingJar.balance shouldBe 0.toMoney
    bankAccount.balance shouldBe 0.toMoney

  test("SavingJarImpl should initialize with correct initial values"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    savingJar.annualInterest shouldBe 0.05
    savingJar.currency.code shouldBe "EUR"
    savingJar.monthlyDeposit shouldBe 100.toMoney
    savingJar.setMonthlyDeposit(200.toMoney)
    savingJar.monthlyDeposit shouldBe 200.toMoney


  test("Deposit should increase balance of savingJar and decrease balance of bankAccount correctly"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    bankAccount.deposit(200.toMoney)
    savingJar.deposit(50.toMoney) shouldBe true
    savingJar.balance shouldBe 50.toMoney
    bankAccount.balance shouldBe 150.toMoney


  test("Withdraw should decrease balance of savingJar and increase balance of bankAccount correctly"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    bankAccount.deposit(200.toMoney)
    savingJar.deposit(50.toMoney)
    savingJar.withdraw(30.toMoney) shouldBe true
    savingJar.balance shouldBe 20.toMoney
    bankAccount.balance shouldBe 180.toMoney


  test("Withdraw should fail if balance is insufficient"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    assertThrows[IllegalArgumentException]:
      savingJar.withdraw(200.toMoney) shouldBe false
    savingJar.balance shouldBe 0.toMoney

  test("ApplyYearInterest should calculate interest correctly"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    bankAccount.deposit(100.toMoney)
    savingJar.deposit(100.toMoney)
    savingJar.applyYearInterest()
    savingJar.balance shouldBe 100.05.toMoney

/*
  //non si riesce a confrontare per errori macchina, inoltre non troncabile poichè MoneyADT todo
  test("AnnualProjection should calculate projected balance for given years"):
    val savingJar = SavingsJar(0.5, 100.toMoney, currency)
    val monthsLeft = 12 - LocalDate.now().getMonthValue + 1
    val res = (((monthsLeft * 100) * 1.005 + (12 * 100)) * 1.005 + (12 * 100)) * 1.005
    savingJar.annualProjection(3).setScale(2) shouldBe res.toMoney.setScale(2)
*/
  test("ChangeCurrency should convert balance correctly with conversion fee"):
    val bankAccount = bank.createBankAccount(customer, bankAccountType, currency)
    val savingJar = SavingsJar(0.05, 100.toMoney, currency, customer.bankAccounts.last)

    bankAccount.deposit(100.toMoney)
    savingJar.deposit(100.toMoney)
    val newCurrency = Currency("USD", "$")
    savingJar.changeCurrency(newCurrency, 5)
    savingJar.currency shouldBe newCurrency
    savingJar.balance shouldBe 95.toMoney

