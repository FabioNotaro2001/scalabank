package scalabank.entities

import scalabank.appointment.Appointment

import java.time.LocalDate

/**
 * Represents a staff member.
 *
 * @tparam T the type of the staff position
 */
trait StaffMember[T <: StaffPosition] extends Person:
  /** The year when member was hired. */
  def hiringYear: Int

  /** The position of the member. */
  def position: T

  /** The salary of the member. */
  def salary: Double = position.salary

  /** The annual salary of the member. */
  def annualSalary: Double = position.salary * 12

  /** The number of years that the member has been in service. */
  def yearsOfService: Int = LocalDate.now.getYear - hiringYear

  /**
   * Calculates the annual net salary of the member.
   *
   * @param taxRate the tax rate
   * @return the annual net salary after reduction
   */
  def annualNetSalary(using taxRate: Double): Double =
    val taxes = annualSalary * taxRate
    annualSalary - taxes

  private var appointments: List[Appointment] = List()
  /** Retrieves all appointments of the member. */
  def getAppointments: Iterable[Appointment] = appointments

  /** Adds a new appointment to the member's schedule.
   *
   * @param appointment the new appointment.
   */
  def addAppointment(appointment: Appointment): Unit =
    appointments = appointments :+ appointment

  /** Removes an existing appointment from the staff member's schedule.
   *
   * @param appointment the appointment to delete.
   * */
  def removeAppointment(appointment: Appointment): Unit =
    appointments = appointments.filterNot(_ == appointment)

  /**
   * Updates an existing appointment with a new one.
   *
   * @param appointment the existing appointment to be updated
   * @param newAppointment the new appointment to replace the old one
   */
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app
