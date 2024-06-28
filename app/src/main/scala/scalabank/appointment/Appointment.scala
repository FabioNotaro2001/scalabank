package scalabank.appointment

import scalabank.entities.{Customer, Employee}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

extension (appointment: Appointment)
  def toStringFromCustomerSide: String =
    s"Appointment with employee ${appointment.employee.cf}, at ${appointment.date.format(dateFormatter)}, of duration ${appointment.duration} minutes. Reason: '${appointment.description}'"
  def toStringFromEmployeeSide: String =
    s"Appointment with customer ${appointment.customer.cf}, at ${appointment.date.format(dateFormatter)}, of duration ${appointment.duration} minutes. Reason: '${appointment.description}'"