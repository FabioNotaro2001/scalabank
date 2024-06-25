import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.currency.{Currency, MoneyADT}
import scalabank.entities.*
import scalabank.currency.MoneyADT.toMoney

import java.time.LocalDate

@RunWith(classOf[JUnitRunner])
class SavingJarImpl extends AnyFunSuite {

  test("SavingJarImpl should initialize with correct initial values"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"))
    savingJar.balance shouldBe 0.toMoney
    savingJar.annualInterest shouldBe 0.05
    savingJar.currency.code shouldBe "EUR"
    savingJar.monthlyDeposit shouldBe 100.toMoney
    savingJar.setMonthlyDeposit(200.toMoney)
    savingJar.monthlyDeposit shouldBe 200.toMoney


  test("Deposit should increase balance correctly"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"))
    savingJar.deposit(50.toMoney)
    savingJar.balance shouldBe 50.toMoney


  test("Withdraw should decrease balance correctly"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"))
    savingJar.deposit(100.toMoney)
    savingJar.withdraw(50.toMoney) shouldBe true
    savingJar.balance shouldBe 50.toMoney


  test("Withdraw should fail if balance is insufficient"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"))
    savingJar.withdraw(200.toMoney) shouldBe false
    savingJar.balance shouldBe 0.toMoney


  test("ApplyYearInterest should calculate interest correctly"):
    val savingJar = SavingsJar(0.05, 100.toMoney, Currency("EUR", "€"))
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
    val savingJar = SavingsJar(0.5, 100.toMoney, Currency("EUR", "€"))
    savingJar.deposit(100.toMoney)
    val newCurrency = Currency("USD", "$")
    savingJar.changeCurrency(newCurrency, 5)
    savingJar.currency shouldBe newCurrency
    savingJar.balance shouldBe 95.toMoney
}
