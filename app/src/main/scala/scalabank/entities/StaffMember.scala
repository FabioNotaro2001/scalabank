package scalabank.entities


abstract class StaffMember[T] extends Person:
  def position: T
  def salary: Double
  def annualSalary: Double = salary * 12

  def annualNetSalary(using taxRate: Double): Double =
    val taxes = annualSalary * taxRate
    annualSalary - taxes




