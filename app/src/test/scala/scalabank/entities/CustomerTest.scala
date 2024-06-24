package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.appointment.Appointment
import scalabank.bank.Bank.{PhysicalBank, PhysicalBankInformation}
import scalabank.currency.Currency
import scalabank.entities.defaultBaseFeeCalculator
import scalabank.entities.*
import scalabank.bankAccount.BankAccount

import java.time.LocalDateTime
import scalabank.currency.MoneyADT.toMoney
import scalabank.entities.Customer.OldCustomerImpl

@RunWith(classOf[JUnitRunner])
class CustomerTest extends AnyFunSuite:
  private val employee = Employee("JHNDOE22B705Y", "Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2015)
  private val duration = 30

  test("Customer should be correctly initialized and age calculated"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1990)
    customer.name shouldBe "John"
    customer.surname shouldBe "Doe"
    customer.birthYear shouldBe 1990
    customer.age shouldBe 34
    customer.isAdult shouldBe true

  test("Customer should be a YoungCustomer if is less 35 years old"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 2000)
    //customer shouldBe a[YoungCustomerImpl]
    customer shouldBe a[Customer]

  test("Customer should be a OldCustomer if is more 65 years old"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1950)
    //customer shouldBe a[OldCustomerImpl]
    customer shouldBe a[Customer]

  test("Customer should be a BaseCustomer if is oldest 35 years old"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    //customer shouldBe a[BaseCustomerImpl]
    customer shouldBe a[Customer]

  test("Customer should be has a fidelity"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    customer.fidelity shouldBe a[Fidelity]

  test("Young Customer should be has a initial baseFee of 0"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 2000)
    customer.baseFee shouldBe 0

  test("Customer should be has a initial baseFee of 1"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    customer.baseFee shouldBe 1

  test("Customer should be able to add appointments"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    val appointment = Appointment(customer, employee, "Meeting", LocalDateTime.now().plusDays(1), duration)
    customer.addAppointment(appointment)
    customer.getAppointments should contain (appointment)

  test("Customer should be able to remove appointments"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    val appointment = Appointment(customer, employee, "Meeting", LocalDateTime.now().plusDays(1), duration)
    customer.addAppointment(appointment)
    customer.removeAppointment(appointment)
    customer.getAppointments should not contain (appointment)

  test("Customer should be able to update appointments"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    val oldAppointment = Appointment(customer, employee, "Meeting", LocalDateTime.now().plusDays(1), duration)
    val newAppointment = Appointment(customer, employee, "Lunch", LocalDateTime.now().plusDays(2), duration)
    customer.addAppointment(oldAppointment)
    customer.updateAppointment(oldAppointment)(newAppointment)
    customer.getAppointments should contain (newAppointment)
    customer.getAppointments should not contain (oldAppointment)

  test("Customer should be able to register a bank"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    val bank = PhysicalBank(PhysicalBankInformation("Cesena Bank", "via Roma 3", "12345678"))
    customer.registerBank(bank)
    customer.bank should contain(bank)

  test("Customer should be able to deregister a bank"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    val bank = PhysicalBank(PhysicalBankInformation("Cesena Bank", "via Roma 3", "12345678"))
    customer.registerBank(bank)
    customer.deregisterBank(bank)
    customer.bank should be(None)

  test("Customer should be able to add bank accounts"):
    val customer = Customer("JHNDOE22B705Y", "John", "Doe", 1980)
    val bank = PhysicalBank(PhysicalBankInformation("Cesena Bank", "via Roma 3", "12345678"))
    bank.addBankAccountType("Base BankAccount", 2.toMoney)
    customer.registerBank(bank)
    customer.addBankAccount(bank.getBankAccountTypes.head, Currency(code = "EUR", symbol = "€"))
    println(customer.bankAccounts)
    customer.bankAccounts.size should be(1)
    customer.bankAccounts.head shouldBe a [BankAccount]
