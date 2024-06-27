package scalabank.database.employee

import scalabank.database.{AbstractCache, Database, DatabaseOperations, PopulateEntityTable}
import scalabank.entities.Employee
import scalabank.entities.Employee.EmployeePosition

import java.sql.{Connection, ResultSet}
import scala.collection.mutable.Map as MutableMap

/**
 * Class representing the employee table in the database.
 *
 * @param connection The database connection to use.
 * @param database The database reference.
 */
class EmployeeTable(override val connection: Connection, override val database: Database) extends AbstractCache[Employee, String] with DatabaseOperations[Employee, String]:
  import database.*

  private val fetchedEmployees = cache

  private val tableCreated =
    if !tableExists("employee", connection) then
      val query = "CREATE TABLE IF NOT EXISTS employee (cf VARCHAR(16) PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), birthYear INT, position VARCHAR(50), hiringYear INT)"
      connection.createStatement.execute(query)
      true
    else false

  override def initialize(): Unit =
    if tableCreated then
      populateDB(2)

  def insert(entity: Employee): Unit =
    val query = "INSERT INTO employee (cf, name, surname, birthYear, position, hiringYear) VALUES (?, ?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.cf)
    stmt.setString(2, entity.name)
    stmt.setString(3, entity.surname)
    stmt.setInt(4, entity.birthYear)
    stmt.setString(5, entity.position.toString)
    stmt.setInt(6, entity.hiringYear)
    stmt.executeUpdate

  private def createEmployee(resultSet: ResultSet) =
    val cf = resultSet.getString("cf")
    fetchedEmployees.get(cf) match
      case Some(e) => e
      case _ =>
        val employee = Employee(cf,
                 resultSet.getString("name"),
                 resultSet.getString("surname"),
                 resultSet.getInt("birthYear"),
                 position = EmployeePosition.valueOf(resultSet.getString("position")),
                 hiringYear = resultSet.getInt("hiringYear"))
        fetchedEmployees.put(employee.cf, employee)
        appointmentTable.findByEmployeeCf(employee.cf).foreach:
          a => employee.addAppointment(a)
        employee

  def findById(cf: String): Option[Employee] =
    val query = "SELECT * FROM employee WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, cf)
    val result = stmt.executeQuery
    for
      _ <- Option(result) if result.next
    yield createEmployee(result)

  def findAll(): Seq[Employee] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM employee"
    val resultSet = stmt.executeQuery(query)
    new Iterator[Employee]:
      def hasNext: Boolean = resultSet.next
      def next(): Employee = createEmployee(resultSet)
    .toSeq

  def update(entity: Employee): Unit =
    val query = "UPDATE employee SET name = ?, surname = ?, birthYear = ?, position = ?, hiringYear = ? WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.name)
    stmt.setString(2, entity.surname)
    stmt.setInt(3, entity.birthYear)
    stmt.setString(4, entity.position.toString)
    stmt.setInt(5, entity.hiringYear)
    stmt.setString(6, entity.cf)
    stmt.executeUpdate
    fetchedEmployees.remove(entity.cf)

  def delete(cf: String): Unit =
    val query = "DELETE FROM employee WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, cf)
    stmt.executeUpdate
    fetchedEmployees.remove(cf)

  private def populateDB(numberOfEntries: Int): Unit =
    PopulateEntityTable.createInstancesDB[Employee](numberOfEntries,
      (cf, name, surname, birthYear) => Employee(cf, name, surname, birthYear, EmployeePosition.Cashier, 2020)
    ).foreach(insert)

