package scalabank.appointment

trait AppointmentBehaviour:
  private var appointments: List[Appointment] = List()

  /** Retrieves all appointments of the member. */
  def getAppointments: Iterable[Appointment] = appointments

  /** Adds a new appointment to the member's schedule.
   *
   * @param appointment the new appointment.
   */
  def addAppointment(appointment: Appointment): Unit =
    appointments = appointments :+ appointment

  /** Removes an existing appointment from the staff member's schedule.
   *
   * @param appointment the appointment to delete.
   * */
  def removeAppointment(appointment: Appointment): Unit =
    appointments = appointments.filterNot(_ == appointment)

  /**
   * Updates an existing appointment with a new one.
   *
   * @param appointment    the existing appointment to be updated
   * @param newAppointment the new appointment to replace the old one
   */
  def updateAppointment(appointment: Appointment)(newAppointment: Appointment): Unit =
    appointments = appointments.map:
      case app if app == appointment => newAppointment
      case app => app
