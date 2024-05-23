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