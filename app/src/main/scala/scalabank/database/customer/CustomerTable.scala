package scalabank.database.customer

import scalabank.database.{Database, DatabaseOperations, PopulateEntityTable}
import scalabank.entities.Customer

import java.sql.{Connection, ResultSet}
import scala.collection.mutable.Map as MutableMap

/**
 * Class representing the customer table in the database.
 *
 * @param connection The database connection to use.
 * @param database The database reference.
 */
class CustomerTable(override val connection: Connection, override val database: Database) extends DatabaseOperations[Customer, String]:
  import database.*

  private val fetchedCustomers = MutableMap[String, Customer]()

  private val tableCreated =
    if !tableExists("customer", connection) then
      val query = "CREATE TABLE IF NOT EXISTS customer (cf VARCHAR(16) PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), birthYear INT)"
      connection.createStatement.execute(query)
      true
    else false

  override def initialize(): Unit =
    if tableCreated then
      populateDB(2)

  def insert(entity: Customer): Unit =
    val query = "INSERT INTO customer (cf, name, surname, birthYear) VALUES (?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.cf)
    stmt.setString(2, entity.name)
    stmt.setString(3, entity.surname)
    stmt.setInt(4, entity.birthYear)
    stmt.executeUpdate

  private def createCustomer(resultSet: ResultSet) =
    val cf = resultSet.getString("cf")
    fetchedCustomers.get(cf) match
      case Some(c) => c
      case None =>
        val customer = Customer(cf, resultSet.getString("name"), resultSet.getString("surname"), resultSet.getInt("birthYear"))
        fetchedCustomers.put(cf, customer)
        appointmentTable.findByCustomerCf(cf).foreach:
          a => customer.addAppointment(a)
        bankAccountTable.findByCustomerCf(cf).foreach:
          acc => customer.addBankAccount(acc)
        customer

  def findById(cf: String): Option[Customer] =
    val query = "SELECT * FROM customer WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, cf)
    val result = stmt.executeQuery
    for
      _ <- Option(result) if result.next
    yield createCustomer(result)

  def findAll(): Seq[Customer] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM customer"
    val resultSet = stmt.executeQuery(query)
    new Iterator[Customer]:
      def hasNext: Boolean = resultSet.next
      def next(): Customer = createCustomer(resultSet)
    .toSeq

  def update(entity: Customer): Unit =
    val query = "UPDATE customer SET name = ?, surname = ?, birthYear = ? WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.name)
    stmt.setString(2, entity.surname)
    stmt.setInt(3, entity.birthYear)
    stmt.setString(4, entity.cf)
    stmt.executeUpdate
    fetchedCustomers.remove(entity.cf)

  def delete(cf: String): Unit =
    val query = "DELETE FROM customer WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, cf)
    stmt.executeUpdate
    fetchedCustomers.remove(cf)

  private def populateDB(numberOfEntries: Int): Unit =
    PopulateEntityTable.createInstancesDB[Customer](numberOfEntries, 
      (cf, name, surname, birthYear) => Customer(cf, name, surname, birthYear)
    ).foreach(insert)
    val customerFixed = Customer("BDE", "Andrea", "Bedei", 2001)
    insert(customerFixed)
