package scalabank.entities

import scalabank.appointment.Appointment

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

  private var appointments: List[Appointment] = List()
  def getAppointments: Iterable[Appointment] = appointments.view
  def addAppointment(appointment: Appointment): Unit =
    appointments = appointments :+ appointment
  def removeAppointment(appointment: Appointment): Unit =
    appointments = appointments.filterNot(_ == appointment)
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app