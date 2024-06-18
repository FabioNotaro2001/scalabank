package scalabank

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.appointment.Appointment
import scalabank.entities.BaseFeeCalculator
import scalabank.entities.defaultBaseFeeCalculator
import scalabank.entities.*
import java.time.LocalDateTime


@RunWith(classOf[JUnitRunner])
class CustomerTest extends AnyFunSuite:
  private val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2015)
  private val duration = 30

  test("Customer should be correctly initialized and age calculated"):
    val customer = Customer("John", "Doe", 1990)
    customer.name shouldBe "John"
    customer.surname shouldBe "Doe"
    customer.birthYear shouldBe 1990
    customer.age shouldBe 34
    customer.isAdult shouldBe true

  test("Customer should be a YoungCustomer if is less 35 years old"):
    val customer = Customer("John", "Doe", 2000)
    customer shouldBe a[YoungCustomer]
    customer shouldBe a[Customer]

  test("Customer should be a OldCustomer if is more 65 years old"):
    val customer = Customer("John", "Doe", 1950)
    customer shouldBe a[OldCustomer]
    customer shouldBe a[Customer]

  test("Customer should be a BaseCustomer if is oldest 35 years old"):
    val customer = Customer("John", "Doe", 1980)
    customer shouldBe a[BaseCustomer]
    customer shouldBe a[Customer]

  test("Customer should be has a fidelity"):
    val customer = Customer("John", "Doe", 1980)
    customer.fidelity shouldBe a[Fidelity]

  test("Young Customer should be has a initial baseFee of 0"):
    val customer = Customer("John", "Doe", 2000)
    customer.baseFee shouldBe 0

  test("Customer should be has a initial baseFee of 1"):
    val customer = Customer("John", "Doe", 1980)
    customer.baseFee shouldBe 1

  test("Customer should be able to add appointments"):
    val customer = Customer("John", "Doe", 1980)
    val appointment = Appointment(customer, employee, "Meeting", LocalDateTime.now(), duration)
    customer.addAppointment(appointment)
    customer.getAppointments() should contain (appointment)

  test("Customer should be able to remove appointments"):
    val customer = Customer("John", "Doe", 1980)
    val appointment = Appointment(customer, employee, "Meeting", LocalDateTime.now(), duration)
    customer.addAppointment(appointment)
    customer.removeAppointment(appointment)
    customer.getAppointments() should not contain (appointment)

  test("Customer should be able to update appointments"):
    val customer = Customer("John", "Doe", 1980)
    val oldAppointment = Appointment(customer, employee, "Meeting", LocalDateTime.now(), duration)
    val newAppointment = Appointment(customer, employee, "Lunch", LocalDateTime.now().plusHours(1), duration)
    customer.addAppointment(oldAppointment)
    customer.updateAppointment(oldAppointment)(newAppointment)
    customer.getAppointments() should contain (newAppointment)
    customer.getAppointments() should not contain (oldAppointment)
