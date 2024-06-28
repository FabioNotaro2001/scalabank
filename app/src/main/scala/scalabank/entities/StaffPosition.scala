package scalabank.entities

/**
 * Represents a position held by a staff member, including the salary.
 */
trait StaffPosition:

  /**
   * The salary associated with the staff position.
   *
   * @return the salary as a Double.
   */
  def salary: Double