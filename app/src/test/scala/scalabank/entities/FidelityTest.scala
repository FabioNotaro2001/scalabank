package scalabank.entities

package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FidelityImplTest extends AnyFunSuite:

  test("FidelityImpl should be correctly initialized with points and pointsUsed") :
    val fidelity = Fidelity.FidelityImpl(500)
    fidelity.points shouldBe 500
    fidelity.pointsUsed shouldBe 0

  test("addPoints should correctly increase the points") :
    val fidelity = Fidelity.FidelityImpl(500)
    fidelity.addPoints(100)
    fidelity.points shouldBe 600

  test("redeemPoints should correctly deduct points when enough points available") :
    val fidelity = Fidelity.FidelityImpl(500)
    val redeemed = fidelity.redeemPoints(300)
    redeemed shouldBe true
    fidelity.points shouldBe 200
    fidelity.pointsUsed shouldBe 300

  test("redeemPoints should not deduct points when not enough points available") :
    val fidelity = Fidelity.FidelityImpl(500)
    val redeemed = fidelity.redeemPoints(700)
    redeemed shouldBe false
    fidelity.points shouldBe 500
    fidelity.pointsUsed shouldBe 0