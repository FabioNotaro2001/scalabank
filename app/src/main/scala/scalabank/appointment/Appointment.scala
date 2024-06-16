package scalabank.appointment

import scalabank.entities.{Customer, Employee}

import java.time.LocalDateTime

trait Appointment:
  def customer: Customer
  def employee: Employee
  def description: String
  def date: LocalDateTime
  /**
   * Returns the duration of the appointment
   * @return the duration of the appointment expressed in minutes
   */
  def duration: Int

object Appointment:
  def apply(customer: Customer, employee: Employee, description: String, date: LocalDateTime, duration: Int): Appointment = AppointmentImpl(customer, employee, description, date, duration)
  private case class AppointmentImpl(override val customer: Customer, override val employee: Employee, override val description: String, override val date: LocalDateTime, override val duration: Int) extends Appointment:
    require(customer != null, "The customer must be defined")
    require(employee != null, "The employee must be defined")
    require(date != null && date.isAfter(LocalDateTime.now), "Date of appointment has to be valid and in the future")
    require(duration > 0, "Duration of the appointment must be positive")