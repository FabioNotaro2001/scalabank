package scalabank.bank

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.appointment.Appointment
import scalabank.entities.{Customer, Employee}
import scalabank.entities.Employee.EmployeePosition

import java.time.LocalDateTime

@RunWith(classOf[JUnitRunner])
class BankTest extends AnyFlatSpec with BeforeAndAfterEach:
  private val employee = Employee("JHNDEO65B22D705Y", "John", "Doe", 1990, EmployeePosition.FinancialAnalyst, 2000)
  private val customer = Customer("BKRMRK65B22D705Y", "Mark", "Baker", 1990)
  private val duration = 30

  "A physical bank" should "support creating appointments" in:
    val bank: Bank = Bank.physicalBank("Bank", "22 Test St.", "111-2223333")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    val appointment = bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)
    customer.getAppointments should contain (appointment)
    employee.getAppointments should contain (appointment)

  "An appointment" should "be cancellable within due time" in:
    val bank: Bank = Bank.physicalBank("Bank", "22 Test St.", "111-2223333")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    val appointment = bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)
    bank.cancelAppointment(appointment)
    customer.getAppointments shouldNot contain (appointment)
    employee.getAppointments shouldNot contain (appointment)

  "An appointment" should "be modifiable within due time" in :
    val bank: Bank = Bank.physicalBank("Bank", "22 Test St.", "111-2223333")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    val appointment = bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)
    val newAppointment = bank.updateAppointment(appointment, None, Some(LocalDateTime.now.plusDays(2)), None)
    customer.getAppointments should contain(newAppointment)
    employee.getAppointments should contain(newAppointment)
    customer.getAppointments shouldNot contain(appointment)
    employee.getAppointments shouldNot contain(appointment)

  "Unregistered appointments cannot be updated" should "not be modifiable" in:
    val bank: Bank = Bank.physicalBank("Bank", "22 Test St.", "111-2223333")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    val appointment = Appointment(customer, employee, "", LocalDateTime.now.plusDays(1), duration)
    assertThrows[IllegalArgumentException]:
      bank.updateAppointment(appointment, None, Some(LocalDateTime.now.plusDays(2)), None)
    bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)

  "Appointment creation" should "fail if no employees are set" in :
    val bank: Bank = Bank.physicalBank("Bank", "22 Test St.", "111-2223333")
    bank.addCustomer(customer)
    assertThrows[AssertionError]:
      bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)

  "An appointment update" should "actually update fields" in:
    val bank: Bank = Bank.physicalBank("Bank", "22 Test St.", "111-2223333")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    val appointment = bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)
    assertThrows[IllegalArgumentException]:
      val newAppointment = bank.updateAppointment(appointment, None, None, None)

  "A virtual bank" should "not have appointments" in:
    val bank: Bank = Bank.virtualBank("Bank", "111-2223333")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    assertThrows[UnsupportedOperationException]:
      bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1), duration)
    assertThrows[UnsupportedOperationException]:
      bank.updateAppointment(null, None, None, None)
    assertThrows[UnsupportedOperationException]:
      bank.cancelAppointment(null)


