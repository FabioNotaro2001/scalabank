import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.bank.Bank.{PhysicalBank, PhysicalBankInformation}
import scalabank.bankAccount.BankAccount
import scalabank.currency.{Currency, MoneyADT}
import scalabank.entities.*
import scalabank.currency.MoneyADT.toMoney

import java.time.LocalDate

@RunWith(classOf[JUnitRunner])
class SavingJarImpl extends AnyFunSuite {

  val customer = Customer("JHNDOE22B705Y", "John", "Doe", 2000)
  val bank = PhysicalBank(PhysicalBankInformation("Cesena Bank", "via Roma 3", "12345678"))
  bank.addBankAccountType("Base BankAccount", 2.toMoney)
  customer.registerBank(bank)
  customer.addBankAccount(bank.getBankAccountTypes.head, Currency(code = "EUR", symbol = "€"))
  val bankAccount = customer.bankAccounts.head

  test("SavingJarImpl should initialize with correct initial values"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"), bankAccount)
    savingJar.balance shouldBe 0.toMoney
    savingJar.annualInterest shouldBe 0.05
    savingJar.currency.code shouldBe "EUR"
    savingJar.monthlyDeposit shouldBe 100.toMoney
    savingJar.setMonthlyDeposit(200.toMoney)
    savingJar.monthlyDeposit shouldBe 200.toMoney


  test("Deposit should increase balance of savingJar and decrease balance of bankAccount correctly"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"), customer.bankAccounts.head)
    bankAccount.deposit(150.toMoney)
    bankAccount.balance shouldBe 150.toMoney

    savingJar.deposit(50.toMoney)
    savingJar.balance shouldBe 50.toMoney
    bankAccount.balance shouldBe 100.toMoney


  test("Withdraw should decrease balance of savingJar and increase balance of bankAccount correctly"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"), customer.bankAccounts.head)
    bankAccount.balance shouldBe 100.toMoney
    savingJar.deposit(100.toMoney)
    savingJar.withdraw(50.toMoney) shouldBe true

    savingJar.balance shouldBe 50.toMoney
    bankAccount.balance shouldBe 50.toMoney


  test("Withdraw should fail if balance is insufficient"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"), customer.bankAccounts.head)
    savingJar.withdraw(200.toMoney) shouldBe false
    savingJar.balance shouldBe 0.toMoney


  test("Deposit should fail if balance of bankAccount is insufficient"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"), customer.bankAccounts.head)
    savingJar.deposit(200.toMoney) shouldBe false
    savingJar.balance shouldBe 0.toMoney

  test("ApplyYearInterest should calculate interest correctly"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"), customer.bankAccounts.head)
    bankAccount.deposit(100.toMoney)
    savingJar.deposit(100.toMoney)
    savingJar.applyYearInterest()
    savingJar.balance shouldBe 100.05.toMoney

/*
  //non si riesce a confrontare per errori macchina, inoltre non troncabile poichè MoneyADT todo
  test("AnnualProjection should calculate projected balance for given years"):
    val savingJar = SavingsJar(0.5, 100.toMoney, Currency("EUR", "€"))
    val monthsLeft = 12 - LocalDate.now().getMonthValue + 1
    val res = (((monthsLeft * 100) * 1.005 + (12 * 100)) * 1.005 + (12 * 100)) * 1.005
    savingJar.annualProjection(3).setScale(2) shouldBe res.toMoney.setScale(2)
*/
  test("ChangeCurrency should convert balance correctly with conversion fee"):
    val savingJar = SavingsJar(0.5, 100.toMoney, Currency("EUR", "€"), customer.bankAccounts.head)
    bankAccount.deposit(100.toMoney)
    savingJar.deposit(100.toMoney)
    val newCurrency = Currency("USD", "$")
    savingJar.changeCurrency(newCurrency, 5)
    savingJar.currency shouldBe newCurrency
    savingJar.balance shouldBe 95.toMoney
}
