package scalabank.entities


abstract class StaffMember[T] extends Person:
  def position: T
  def salary: Double
  def annualSalary: Double = salary * 12



