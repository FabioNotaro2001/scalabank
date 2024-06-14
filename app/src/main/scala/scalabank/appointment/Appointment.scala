package scalabank.appointment

import scalabank.entities.{Customer, Employee}

import java.time.LocalDateTime

trait Appointment:
  def customer: Customer
  def employee: Employee
  def description: String
  def date: LocalDateTime

object Appointment:
  def apply(customer: Customer, employee: Employee, description: String, date: LocalDateTime): Appointment = AppointmentImpl(customer, employee, description, date)
  private case class AppointmentImpl(override val customer: Customer, override val employee: Employee, override val description: String, override val date: LocalDateTime) extends Appointment