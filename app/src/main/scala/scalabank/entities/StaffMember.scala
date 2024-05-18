package scalabank.entities

import java.time.Year

trait StaffPosition:
  def salary: Double

trait Promotable[T <: StaffPosition]:
  def promote(newPosition: T): Employee

abstract class StaffMember[T <: StaffPosition] extends Person:
  def hiringYear: Int
  def position: T
  def salary: Double = position.salary
  def annualSalary: Double = position.salary * 12
  def yearsOfService: Int = Year.now.getValue - hiringYear

  def annualNetSalary(using taxRate: Double): Double =
    val taxes = annualSalary * taxRate
    annualSalary - taxes


