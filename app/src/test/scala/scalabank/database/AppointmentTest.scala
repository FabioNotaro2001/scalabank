package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.appointment.Appointment
import scalabank.database.appointment.AppointmentTable
import scalabank.database.customer.CustomerTable
import scalabank.database.employee.EmployeeTable

import java.sql.{Connection, DriverManager}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(classOf[JUnitRunner])
class AppointmentTest extends AnyFlatSpec with Matchers:
  private val connection: Connection = DriverManager.getConnection("jdbc:h2:mem:test2;DB_CLOSE_DELAY=-1")
  private val customerTable = new CustomerTable(connection)
  private val employeeTable = new EmployeeTable(connection)
  private val appointmentTable = new AppointmentTable(connection, customerTable, employeeTable)
  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  private def convertDateInFuture(days: Int): LocalDateTime =
    val date = LocalDateTime.now.plusDays(days)
    LocalDateTime.parse(date.format(dateFormat), dateFormat)

  "AppointmentTable" should "insert and retrieve an appointment correctly" in:
    val customers = customerTable.findAll()
    val employees = employeeTable.findAll()
    val customer = customers.head
    val employee = employees.head
    val appointment = Appointment(customer, employee, "Test Appointment", convertDateInFuture(1), 30)
    appointmentTable.insert(appointment)
    val id = (customer.cf, employee.cf, appointment.date)
    val retrievedAppointment = appointmentTable.findById(id)
    retrievedAppointment shouldBe Some(appointment)

  it should "update an appointment correctly" in:
    val customers = customerTable.findAll()
    val employees = employeeTable.findAll()
    val customer = customers.head
    val employee = employees.head
    val appointment = Appointment(customer, employee, "Initial Description", convertDateInFuture(2), 30)
    appointmentTable.insert(appointment)
    val id = (customer.cf, employee.cf, appointment.date)
    val updatedAppointment = Appointment(appointment.customer, appointment.employee, "Update Description", appointment.date, 45)
    appointmentTable.update(updatedAppointment)
    val retrievedAppointment = appointmentTable.findById(id)
    retrievedAppointment shouldBe Some(updatedAppointment)

  it should "delete an appointment correctly" in:
    val customers = customerTable.findAll()
    val employees = employeeTable.findAll()
    val customer = customers.head
    val employee = employees.head
    val appointment = Appointment(customer, employee, "To be deleted", convertDateInFuture(3), 30)
    appointmentTable.insert(appointment)
    val id = (customer.cf, employee.cf, appointment.date)
    appointmentTable.delete(id)
    val retrievedAppointment = appointmentTable.findById(id)
    retrievedAppointment shouldBe None

  it should "find appointments by employeeCf" in:
    val customers = customerTable.findAll()
    val employees = employeeTable.findAll()
    val customer = customers.head
    val employee = employees.head
    val appointment1 = Appointment(customer, employee, "Appointment 1", convertDateInFuture(4), 30)
    val appointment2 = Appointment(customer, employee, "Appointment 2", convertDateInFuture(5), 30)
    appointmentTable.insert(appointment1)
    appointmentTable.insert(appointment2)
    val retrievedAppointments = appointmentTable.findByEmployeeCf(employee.cf)
    retrievedAppointments should contain allOf (appointment1, appointment2)

  it should "find appointments by customerCf" in:
    val customers = customerTable.findAll()
    val employees = employeeTable.findAll()
    val customer = customers.head
    val employee = employees.head
    val appointment1 = Appointment(customer, employee, "Appointment 1", convertDateInFuture(6), 30)
    val appointment2 = Appointment(customer, employee, "Appointment 2", convertDateInFuture(7), 30)
    appointmentTable.insert(appointment1)
    appointmentTable.insert(appointment2)
    val retrievedAppointments = appointmentTable.findByCustomerCf(customer.cf)
    retrievedAppointments should contain allOf (appointment1, appointment2)



