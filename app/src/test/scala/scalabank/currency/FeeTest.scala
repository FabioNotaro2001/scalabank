package scalabank.currency

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldEqual
import org.scalatestplus.junit.JUnitRunner
import scalabank.currency.MoneyADT.*

@RunWith(classOf[JUnitRunner])
class FeeTest extends AnyFlatSpec with BeforeAndAfterEach:

  "FeeManager" should "apply a percentage fee correctly" in:
    val amount: Money = 1000.toMoney
    val feePercentage: BigDecimal = 10
    val result = FeeManager.applyPercentageFee(amount, feePercentage)
    result shouldEqual 900.toMoney

  it should "apply a fixed fee correctly" in:
    val amount: Money = 1000.toMoney
    val fee: Money = 50.toMoney
    val result = FeeManager.applyFee(amount, fee)
    result shouldEqual 950.toMoney

  it should "calculate amount with fixed fee correctly" in:
    val amount: Money = 1000.toMoney
    val fee: Money = 50.toMoney
    val result = FeeManager.calculateAmountWithFee(amount, fee)
    result shouldEqual 1050.toMoney

  it should "calculate amount with percentage fee correctly" in:
    val amount: Money = 1000.toMoney
    val feePercentage: BigDecimal = 10
    val result = FeeManager.calculateAmountWithPercentageFee(amount, feePercentage)
    result shouldEqual 1100.toMoney

  it should "handle zero percentage fee correctly" in:
    val amount: Money = 1000.toMoney
    val feePercentage: BigDecimal = 0
    val result = FeeManager.applyPercentageFee(amount, feePercentage)
    result shouldEqual amount
    val resultWithAdd = FeeManager.calculateAmountWithPercentageFee(amount, feePercentage)
    resultWithAdd shouldEqual amount

  it should "handle zero fixed fee correctly" in:
    val amount: Money = 1000.toMoney
    val fee: Money = 0.toMoney
    val result = FeeManager.applyFee(amount, fee)
    result shouldEqual amount
    val resultWithAdd = FeeManager.calculateAmountWithFee(amount, fee)
    resultWithAdd shouldEqual amount

  it should "handle negative percentage fee correctly" in:
    val amount: Money = 1000.toMoney
    val feePercentage: BigDecimal = -10
    val result = FeeManager.applyPercentageFee(amount, feePercentage)
    result shouldEqual 1100.toMoney
    val resultWithAdd = FeeManager.calculateAmountWithPercentageFee(amount, feePercentage)
    resultWithAdd shouldEqual 900.toMoney
