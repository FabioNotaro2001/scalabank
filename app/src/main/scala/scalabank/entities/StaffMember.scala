package scalabank.entities

trait StaffPosition:
  def salary: Double

abstract class StaffMember[T <: StaffPosition] extends Person:
  def position: T
  def salary: Double = position.salary
  def annualSalary: Double = position.salary * 12

  def annualNetSalary(using taxRate: Double): Double =
    val taxes = annualSalary * taxRate
    annualSalary - taxes


