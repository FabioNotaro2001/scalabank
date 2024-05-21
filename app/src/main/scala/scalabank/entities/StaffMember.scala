package scalabank.entities

import scalabank.appointment.Appointment

import java.time.LocalDate

/**
 * Represents a staff member.
 *
 * @tparam T the type of the staff position
 */
trait StaffMember[T <: StaffPosition]:
  /** The year when member was hired. */
  def hiringYear: Int

  /** The position of the member. */
  def position: T

  /** The salary of the member. */
  def salary: Double

  /** The annual salary of the member. */
  def annualSalary: Double

  /** The number of years that the member has been in service. */
  def yearsOfService: Int = LocalDate.now.getYear - hiringYear

  /**
   * Calculates the annual net salary of the member.
   *
   * @param taxRate the tax rate
   * @return the annual net salary after reduction
   */
  def annualNetSalary(using taxRate: Double): Double

  /** Retrieves all appointments of the member. */
  def getAppointments: Iterable[Appointment]

  /** Adds a new appointment to the member's schedule.
   *
   * @param appointment the new appointment.
   */
  def addAppointment(appointment: Appointment): Unit

  /** Removes an existing appointment from the staff member's schedule.
   *
   * @param appointment the appointment to delete.
   * */
  def removeAppointment(appointment: Appointment): Unit

  /**
   * Updates an existing appointment with a new one.
   *
   * @param appointment the existing appointment to be updated
   * @param newAppointment the new appointment to replace the old one
   */
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit
