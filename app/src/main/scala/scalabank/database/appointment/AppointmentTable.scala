package scalabank.database.appointment

import scalabank.database.DatabaseOperations
import scalabank.appointment.Appointment
import scalabank.database.customer.CustomerTable
import scalabank.database.employee.EmployeeTable
import scalabank.entities.{Customer, Employee}

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AppointmentTable(val connection: Connection, customerTable: CustomerTable, employeeTable: EmployeeTable) extends DatabaseOperations[Appointment, Int]:
  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  if !tableExists("appointment", connection) then
    val query = "CREATE TABLE IF NOT EXISTS appointment (id INT AUTO_INCREMENT PRIMARY KEY, customerCf VARCHAR(16), employeeCf VARCHAR(16), description TEXT, date DATETIME, duration INT)"
    connection.createStatement.execute(query)
  // populateDB(1) // Uncomment if you want to populate initial data

  def insert(entity: Appointment): Unit =
    val query = "INSERT INTO appointment (customerCf, employeeCf, description, date, duration) VALUES (?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
    stmt.setString(1, entity.customer.cf)
    stmt.setString(2, entity.employee.cf)
    stmt.setString(3, entity.description)
    stmt.setString(4, entity.date.format(dateFormat))
    stmt.setInt(5, entity.duration)
    stmt.executeUpdate

  private def createAppointment(resultSet: ResultSet): Appointment =
    val customer = customerTable.findById(resultSet.getString("customerCf")).get
    val employee = employeeTable.findById(resultSet.getString("employeeCf")).get
    null

  def findById(id: Int): Option[Appointment] =
    val query = "SELECT * FROM appointment WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    val result = stmt.executeQuery()
    for
      _ <- Option(result) if result.next
    yield createAppointment(result)

  def findAll(): Seq[Appointment] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM appointment"
    val resultSet = stmt.executeQuery(query)
    new Iterator[Appointment]:
      def hasNext: Boolean = resultSet.next
      def next(): Appointment = createAppointment(resultSet)
    .toSeq

  def update(appointment: Appointment): Unit =
    val query = "UPDATE appointment SET customerCf = ?, employeeCf = ?, description = ?, date = ?, duration = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, appointment.customer.cf)
    stmt.setString(2, appointment.employee.cf)
    stmt.setString(3, appointment.description)
    stmt.setString(4, appointment.date.format(dateFormat))
    stmt.setInt(5, appointment.duration)
    stmt.executeUpdate

  def delete(id: Int): Unit =
    val query = "DELETE FROM appointment WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    stmt.executeUpdate

  def findByEmployeeCf(employeeCf: String): Seq[Appointment] =
    val stmt = connection.prepareStatement("SELECT * FROM appointment WHERE employeeCf = ?")
    findByCf(stmt, employeeCf)

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