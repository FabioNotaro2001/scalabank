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
  val employee = Employee("Alice", "Johnson", 1992, Employee.EmployeePosition.Cashier, 2015)
  private val duration = 30

  test("Appointments should have valid parameters"):
    Appointment(customer, employee, "Meeting with client", LocalDateTime.now.plusDays(1), duration)
    assertThrows[IllegalArgumentException]:
      Appointment(null, employee, "Meeting with client", LocalDateTime.now.plusDays(1), duration)
    assertThrows[IllegalArgumentException]:
      Appointment(customer, null, "Meeting with client", LocalDateTime.now.plusDays(1), duration)
    assertThrows[IllegalArgumentException]:
      Appointment(customer, employee, "Meeting with client", null, duration)
    assertThrows[IllegalArgumentException]:
      Appointment(customer, employee, "Meeting with client", LocalDateTime.now.minusDays(1), duration)
    assertThrows[IllegalArgumentException]:
      Appointment(customer, employee, "Meeting with client", LocalDateTime.now.plusDays(1), -1)
  
  test("addAppointment should add an appointment to the employee's appointments list"):
    val appointment = Appointment(customer, employee, "Meeting with client", LocalDateTime.now.plusDays(1), duration)
    employee.addAppointment(appointment)
    employee.getAppointments should contain(appointment)

  test("removeAppointment should remove an appointment from the employee's appointments list"):
    val appointment = Appointment(customer, employee, "Meeting with client", LocalDateTime.now.plusDays(1), duration)
    employee.addAppointment(appointment)
    employee.removeAppointment(appointment)
    employee.getAppointments should not contain (appointment)

  test("updateAppointment should update the date of an appointment in the employee's appointments list"):
    val appointment = Appointment(customer, employee, "Meeting with client", LocalDateTime.now.plusDays(1), duration)
    employee.addAppointment(appointment)
    val updatedAppointment = Appointment(customer, employee, "Updated meeting with client", LocalDateTime.now.plusDays(2), duration)
    employee.updateAppointment(appointment)(updatedAppointment)
    employee.getAppointments should contain(updatedAppointment)
    employee.getAppointments should not contain (appointment)

