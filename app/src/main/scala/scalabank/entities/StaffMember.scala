package scalabank.entities

import scalabank.appointment.AppointmentBehaviour

import java.time.LocalDate

/**
 * Represents a staff member.
 *
 * @tparam T the type of the staff position
 */
trait StaffMember[T <: StaffPosition] extends Person with AppointmentBehaviour:
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
