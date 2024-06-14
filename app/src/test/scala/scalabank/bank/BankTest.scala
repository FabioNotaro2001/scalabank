package scalabank.bank

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.entities.{Customer, Employee}
import scalabank.entities.Employee.EmployeePosition

import java.awt.FlowLayout
import java.time.LocalDateTime

@RunWith(classOf[JUnitRunner])
class BankTest extends AnyFlatSpec with BeforeAndAfterEach:
  private val employee = Employee("John", "Doe", 1990, EmployeePosition.FinancialAnalyst, 2000)
  private val customer = Customer("Mark", "Baker", 1990)

  "A physical bank" should "support creating appointments" in:
    val bank: Bank = Bank.physicalBank("")
    bank.addCustomer(customer)
    bank.addEmployee(employee)
    val appointment = bank.createAppointment(customer, "", LocalDateTime.now.plusDays(1))
    customer.getAppointments() should contain (appointment)
    employee.getAppointments should contain (appointment)
