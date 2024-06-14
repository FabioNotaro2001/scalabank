package scalabank.appointment

import java.time.LocalDateTime

trait Appointment:
  def description: String
  def date: LocalDateTime

object Appointment:
  def apply(description: String, date: LocalDateTime): Appointment = AppointmentImpl(description, date)

  private case class AppointmentImpl(override val description: String, override val date: LocalDateTime) extends Appointment
