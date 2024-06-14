package scalabank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.matchers.should.Matchers.*
import scalabank.appointment.Appointment

import java.time.LocalDateTime

@RunWith(classOf[JUnitRunner])
class AppointmentTest extends AnyFunSuite:
  private val customer = Customer("John", "Doe", 1990)
  
  test("addAppointment should add an appointment to the employee's appointments list"):
    val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2015)
    val appointment = Appointment(customer, employee, "Meeting with client", LocalDateTime.of(2023, 5, 20, 10, 0))
    employee.addAppointment(appointment)
    employee.getAppointments should contain(appointment)

  test("removeAppointment should remove an appointment from the employee's appointments list"):
    val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2015)
    val appointment = Appointment(customer, employee, "Meeting with client", LocalDateTime.of(2023, 5, 20, 10, 0))
    employee.addAppointment(appointment)
    employee.removeAppointment(appointment)
    employee.getAppointments should not contain (appointment)

  test("updateAppointment should update the date of an appointment in the employee's appointments list"):
    val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2015)
    val appointment = Appointment(customer, employee, "Meeting with client", LocalDateTime.of(2023, 5, 20, 10, 0))
    employee.addAppointment(appointment)
    val updatedAppointment = Appointment(customer, employee, "Updated meeting with client", LocalDateTime.of(2023, 5, 20, 11, 0))
    employee.updateAppointment(appointment)(updatedAppointment)
    employee.getAppointments should contain(updatedAppointment)
    employee.getAppointments should not contain (appointment)

