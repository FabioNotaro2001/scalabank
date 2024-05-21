package scalabank.entities

import scalabank.appointment.Appointment

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

  private var appointments: List[Appointment] = List()

  override def getAppointments: Iterable[Appointment] = appointments

  override def addAppointment(appointment: Appointment): Unit =
    appointments = appointments :+ appointment

  override def removeAppointment(appointment: Appointment): Unit =
    appointments = appointments.filterNot(_ == appointment)

  override def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app