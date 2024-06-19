package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers.a
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.{Customer, Employee}
import scalabank.appointment.Appointment
import scalabank.entities.Employee.EmployeePosition

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(classOf[JUnitRunner])
class AppointmentTest extends AnyFlatSpec with Matchers:
  val db: Database = Database("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
  val customer: Customer = Customer("RSSMRA40L67T890M", "Mario", "Rossi", 1990)
  db.customerTable.insert(customer)
  val employee: Employee = Employee("VRDLGU5L67T830MY", "Luigi", "Verdi", 1985, EmployeePosition.Cashier, 2010)
  db.employeeTable.insert(employee)

  val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  val appointmentDate: LocalDateTime = LocalDateTime.parse(LocalDateTime.now.plusDays(1).format(dateFormat), dateFormat)
  val appointment: Appointment = Appointment(customer, employee, "Consultation", appointmentDate, 60)

  "AppointmentTable" should "insert and find an appointment" in:
    db.appointmentTable.insert(appointment)
    val retrievedAppointment = db.appointmentTable.findById(1)
    retrievedAppointment.get.date shouldEqual appointmentDate

  it should "update an appointment" in:
    val updatedAppointment = Appointment(customer, employee, "Updated Consultation", appointmentDate, 120)
    a [ NotImplementedError ] should be thrownBy db.appointmentTable.update(updatedAppointment)

  it should "delete an appointment" in:
    db.appointmentTable.delete(1)
    val retrievedAppointment = db.appointmentTable.findById(1)
    retrievedAppointment should be(empty)

  it should "find all appointments" in:
    val appointment1 = Appointment(customer, employee, "Consultation 1", appointmentDate, 30)
    val appointment2 = Appointment(customer, employee, "Consultation 2", appointmentDate, 45)
    db.appointmentTable.insert(appointment1)
    db.appointmentTable.insert(appointment2)
    val allAppointments = db.appointmentTable.findAll()
    allAppointments should contain allOf (appointment1, appointment2)

  it should "find appointments by employee CF" in:
    val employee2 = Employee("EMP65420A12B345C", "Anna", "Neri", 1987, EmployeePosition.Cashier, 2012)
    db.employeeTable.insert(employee2)
    val appointment3 = Appointment(customer, employee2, "Consultation 3", appointmentDate, 30)
    db.appointmentTable.insert(appointment3)
    val appointmentsByEmployee = db.appointmentTable.findByEmployeeCf("EMP65420A12B345C")
    appointmentsByEmployee should contain(appointment3)

  it should "find appointments by customer CF" in:
    val customer2 = Customer("CST67824A12B345C", "Laura", "Bianchi", 1992)
    db.customerTable.insert(customer2)
    val appointment4 = Appointment(customer2, employee, "Consultation 4", appointmentDate, 45)
    db.appointmentTable.insert(appointment4)
    val appointmentsByCustomer = db.appointmentTable.findByCustomerCf("CST67824A12B345C")
    appointmentsByCustomer should contain(appointment4)
