package scalabank.appointment

import scalabank.entities.{Customer, Employee}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

import java.time.LocalDateTime

/**
 * Represents an appointment a customer can schedule
 */
trait Appointment:
  /**
   * @return the customer who scheduled the appointment
   */
  def customer: Customer

  /**
   * @return the employee involved in the appointment
   */
  def employee: Employee

  /**
   * @return the description associated to the appointment
   */
  def description: String

  /**
   * @return the date of the appointment
   */
  def date: LocalDateTime
  
  /**
   * @return the duration of the appointment expressed in minutes
   */
  def duration: Int

trait AppointmentComponent:
  loggerDependency: LoggerDependency =>
  protected case class AppointmentImpl(override val customer: Customer, override val employee: Employee, override val description: String, override val date: LocalDateTime, override val duration: Int) extends Appointment:
    require(customer != null, "The customer must be defined")
    require(employee != null, "The employee must be defined")
    require(duration > 0, "Duration of the appointment must be positive")
    loggerDependency.logger.log(loggerDependency.logger.getPrefixFormatter().getCreationPrefix + this)

object Appointment extends LoggerDependency with AppointmentComponent:
  override val logger: Logger = LoggerImpl()
  def apply(customer: Customer, employee: Employee, description: String, date: LocalDateTime, duration: Int): Appointment = AppointmentImpl(customer, employee, description, date, duration)
