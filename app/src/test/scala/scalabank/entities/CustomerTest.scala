package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*


@RunWith(classOf[JUnitRunner])
class CustomerTest extends AnyFunSuite:
  test("Customer should be correctly initialized and age calculated"):
    val customer = Customer("John", "Doe", 1990)
    customer.name shouldBe "John"
    customer.surname shouldBe "Doe"
    customer.birthYear shouldBe 1990
    customer.age shouldBe 34
    customer.isAdult shouldBe true

  test("Customer should be a YoungCustomer if is less 35 years old"):
    val customer = Customer("John", "Doe", 2000)
    assert(customer.isInstanceOf[YoungCustomer])
    assert(customer.isInstanceOf[Customer])

  test("Customer should be a BaseCustomer if is oldest 35 years old"):
    val customer = Customer("John", "Doe", 1980)
    assert(customer.isInstanceOf[BaseCustomer])
    assert(customer.isInstanceOf[Customer])

  test("Customer should be has a fidelity"):
    val customer = Customer("John", "Doe", 1980)
    assert(customer.fidelity.isInstanceOf[Fidelity])

  test("Young Customer should be has a initial baseFee of 0"):
    val customer = Customer("John", "Doe", 2000)
    customer.baseFee shouldBe 0

  test("Customer should be has a initial baseFee of 1"):
    val customer = Customer("John", "Doe", 1980)
    customer.baseFee shouldBe 1

    