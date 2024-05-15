package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*

@RunWith(classOf[JUnitRunner])
class CustomerTest extends AnyFunSuite{
  test("Customer should be correctly initialized and age calculated"):
    val customer = Customer("John", "Doe", 1990)

    customer.name shouldBe "John"
    customer.surname shouldBe "Doe"
    customer.birthYear shouldBe 1990
    customer.isAdult shouldBe true
}
