package scalabank.entities

import scalabank.appointment.Appointment

import java.time.LocalDate

/**
 * Represents a position held by a staff member, including the salary.
 */
trait StaffPosition:
  def salary: Double

/**
 * Represents the promotion a staff member to a new position.
 *
 * @tparam T the type of the staff position
 */
trait Promotable[T <: StaffPosition]:
  def promote(newPosition: T): Employee

/**
 * Represents a staff member.
 *
 * @tparam T the type of the staff position
 */
trait StaffMember[T]:
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

/**
 * An abstract implementation of a staff member, providing common functionality.
 *
 * @tparam T the type of the staff position
 */
abstract class AbstractStaffMember[T <: StaffPosition] extends Person with StaffMember[T]:

  override def salary: Double = position.salary

  override def annualSalary: Double = position.salary * 12

  override def annualNetSalary(using taxRate: Double): Double =
    val taxes = annualSalary * taxRate
    annualSalary - taxes

  private var appointments: Vector[Appointment] = Vector()

  override def getAppointments: Iterable[Appointment] = appointments.view

  override def addAppointment(appointment: Appointment): Unit =
    appointments = appointments :+ appointment

  override def removeAppointment(appointment: Appointment): Unit =
    appointments = appointments.filterNot(_ == appointment)

  override def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app