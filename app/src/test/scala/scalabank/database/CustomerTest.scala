package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.Customer

@RunWith(classOf[JUnitRunner])
class CustomerTest extends AnyFlatSpec with Matchers:
  val db: Database = Database("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
  val customer: Customer = Customer("CST12345L67T890M", "Mario", "Rossi", 1990)

  "CustomerTable" should "insert and find a customer" in:
    db.customerTable.insert(customer)
    val retrievedCustomer = db.customerTable.findById("CST12345L67T890M")
    retrievedCustomer should be(defined)
    retrievedCustomer.get shouldEqual customer

  it should "update a customer" in:
    val updatedCustomer = Customer("CST12345L67T890M", "Luigi", "Bianchi", 1990)
    db.customerTable.update(updatedCustomer)
    val retrievedCustomer = db.customerTable.findById("CST12345L67T890M")
    retrievedCustomer should be(defined)
    retrievedCustomer.get.name shouldEqual "Luigi"
    retrievedCustomer.get.surname shouldEqual "Bianchi"

  it should "delete a customer" in:
    db.customerTable.delete("CST12345L67T890M")
    val retrievedCustomer = db.customerTable.findById("CST12345L67T890M")
    retrievedCustomer should be(empty)

  it should "find all customers" in:
    val customer1 = Customer("CST67890A12B345C", "Mario", "Pastori", 1990)
    val customer2 = Customer("CST09876D34E567F", "Luigi", "Verdi", 1985)
    db.customerTable.insert(customer1)
    db.customerTable.insert(customer2)
    val allCustomers = db.customerTable.findAll()
    allCustomers should contain allOf (customer1, customer2)
