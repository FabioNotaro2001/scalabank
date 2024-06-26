package scalabank.database.appointment

import scalabank.database.{Database, DatabaseOperations}
import scalabank.appointment.Appointment
import scalabank.database.customer.CustomerTable
import scalabank.database.employee.EmployeeTable

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.collection.mutable.Map as MutableMap

/**
 * Class representing the appointment table in the database.
 *
 * @param connection    The database connection to use.
 * @param database The database reference.
 */
class AppointmentTable(override val connection: Connection, override val database: Database) extends DatabaseOperations[Appointment, (String, String, LocalDateTime)]:
  import database.*

  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val fetchedAppointments = MutableMap[(String, String, String), Appointment]()

  private val tableCreated =
    if !tableExists("appointment", connection) then
      val query = "CREATE TABLE IF NOT EXISTS appointment ( customerCf VARCHAR(16), employeeCf VARCHAR(16)," +
        " description TEXT, date DATETIME, duration INT, PRIMARY KEY (customerCf, employeeCf, date))"
      connection.createStatement.execute(query)
      true
    else false

  override def initialize(): Unit =
    if tableCreated then
      populateDB()

  def insert(entity: Appointment): Unit =
    val query = "INSERT INTO appointment (customerCf, employeeCf, description, date, duration) VALUES (?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.customer.cf)
    stmt.setString(2, entity.employee.cf)
    stmt.setString(3, entity.description)
    stmt.setString(4, entity.date.format(dateFormat))
    stmt.setInt(5, entity.duration)
    stmt.executeUpdate

  private def createAppointment(resultSet: ResultSet): Appointment =
    val customer = customerTable.findById(resultSet.getString("customerCf")).get
    val employee = employeeTable.findById(resultSet.getString("employeeCf")).get
    val dateString = resultSet.getString("date")
    val key = (customer.cf, employee.cf, dateString)
    fetchedAppointments.get(key) match
      case Some(a) => a
      case None =>
        val appointment = Appointment(customer,
                    employee,
                    resultSet.getString("description"),
                    LocalDateTime.parse(dateString, dateFormat),
                    resultSet.getInt("duration"))
        fetchedAppointments.put(key, appointment)
        appointment

  def findById(id: (String, String, LocalDateTime)): Option[Appointment] =
    val (customerCf, employeeCf, date) = id
    val query = "SELECT * FROM appointment WHERE customerCf = ? AND employeeCf = ? AND date = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, customerCf)
    stmt.setString(2, employeeCf)
    stmt.setString(3, date.format(dateFormat))
    val result = stmt.executeQuery
    if result.next then Some(createAppointment(result)) else None

  def findAll(): Seq[Appointment] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM appointment"
    val resultSet = stmt.executeQuery(query)
    new Iterator[Appointment]:
      def hasNext: Boolean = resultSet.next
      def next(): Appointment = createAppointment(resultSet)
    .toSeq

  def update(appointment: Appointment): Unit =
    val query = "UPDATE appointment SET description = ?, duration = ? WHERE customerCf = ? AND employeeCf = ? AND date = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, appointment.description)
    stmt.setInt(2, appointment.duration)
    stmt.setString(3, appointment.customer.cf)
    stmt.setString(4, appointment.employee.cf)
    stmt.setString(5, appointment.date.format(dateFormat))
    stmt.executeUpdate
    fetchedAppointments.remove((appointment.customer.cf, appointment.employee.cf, appointment.date.format(dateFormat)))


  def delete(id: (String, String, LocalDateTime)): Unit =
    val (customerCf, employeeCf, date) = id
    val query = "DELETE FROM appointment WHERE customerCf = ? AND employeeCf = ? AND date = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, customerCf)
    stmt.setString(2, employeeCf)
    stmt.setString(3, date.format(dateFormat))
    stmt.executeUpdate
    fetchedAppointments.remove((customerCf, employeeCf, date.format(dateFormat)))

  /**
   * Finds appointments by the employee's fiscal code.
   *
   * @param employeeCf The fiscal code of the employee.
   * @return A sequence of appointments associated with the given employee fiscal code.
   */
  def findByEmployeeCf(employeeCf: String): Seq[Appointment] =
    val stmt = connection.prepareStatement("SELECT * FROM appointment WHERE employeeCf = ?")
    findByCf(stmt, employeeCf)

  /**
   * Finds appointments by the customer's fiscal code.
   *
   * @param customerCf The fiscal code of the customer.
   * @return A sequence of appointments associated with the given customer fiscal code.
   */
  def findByCustomerCf(customerCf: String): Seq[Appointment] =
    val stmt = connection.prepareStatement("SELECT * FROM appointment WHERE customerCf = ?")
    findByCf(stmt, customerCf)

  private def findByCf(stmt: PreparedStatement, cf: String): Seq[Appointment] =
    stmt.setString(1, cf)
    val resultSet = stmt.executeQuery
    new Iterator[Appointment]:
      def hasNext: Boolean = resultSet.next
      def next(): Appointment = createAppointment(resultSet)
    .toSeq

  private def populateDB(): Unit =
    val customers = customerTable.findAll()
    val employees = employeeTable.findAll()
    val appointments = for
      customer <- customers
      employee <- employees
      i <- 1 to 5
    yield
      val description = s"Appointment $i between ${customer.cf} and ${employee.cf}"
      val date = LocalDateTime.now.plusDays(i)
      val duration = 30
      Appointment(customer, employee, description, date, duration)
    appointments.foreach(insert)
