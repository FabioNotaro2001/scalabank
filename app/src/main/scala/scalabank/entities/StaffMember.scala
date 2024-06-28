package scalabank.entities

import scalabank.appointment.AppointmentBehaviour

import java.time.LocalDate

/**
 * Represents a staff member.
 *
 * @tparam T the type of the staff position
 */
trait StaffMember[T <: StaffPosition] extends Person with AppointmentBehaviour:

  /**
   * The year the staff member was hired.
   * @return the year.
   */
  def hiringYear: Int

  /**
   * The position held by the staff member.
   * @return the position as a subtype of StaffPosition.
   */
  def position: T

  /**
   * The monthly salary of the staff member.
   * @return the salary.
   */
  def salary: Double = position.salary

  /**
   * The annual salary of the staff member.
   * @return the annual salary, calculated as 12 times the monthly salary.
   */
  def annualSalary: Double = position.salary * 12

  /**
   * The number of years the staff member has been in service.
   * @return the number of years, calculated from the current year minus the hiring year.
   */
  def yearsOfService: Int = LocalDate.now.getYear - hiringYear

  /**
   * The annual net salary of the staff member, after deducting taxes.
   * @param taxRate the tax rate to apply.
   * @return the annual net salary as a Double, calculated as the annual salary minus the taxes.
   */
  def annualNetSalary(using taxRate: Double): Double =
    val taxes = annualSalary * taxRate
    annualSalary - taxes
