package scalabank.database

import scalabank.database.appointment.AppointmentTable
import scalabank.database.bank.{BankAccountTable, BankAccountTypeTable}
import scalabank.database.customer.CustomerTable
import scalabank.database.employee.EmployeeTable
import scalabank.database.person.PersonTable
import scalabank.database.interest.*

import java.sql.{Connection, DriverManager}
import scalabank.database.interest
import scalabank.database.movement.MovementTable

/**
 * Trait defining the structure of a database with multiple tables.
 */
trait Database:

  /**
   * Accessor for the person table.
   *
   * @return An instance of the PersonTable.
   */
  def personTable: PersonTable

  /**
   * Accessor for the employee table.
   *
   * @return An instance of the EmployeeTable.
   */
  def employeeTable: EmployeeTable

  /**
   * Accessor for the customer table.
   *
   * @return An instance of the CustomerTable.
   */
  def customerTable: CustomerTable

  /**
   * Accessor for the appointment table.
   *
   * @return An instance of the AppointmentTable.
   */
  def appointmentTable: AppointmentTable

  /**
   * Accessor for the bank account table.
   *
   * @return An instance of the BankAccountTable.
   */
  def bankAccountTable: BankAccountTable

  /**
   * Accessor for the interest table.
   *
   * @return An instance of the InterestTable.
   */
  def interestTable: InterestTable

  /**
   * Accessor for the movement table.
   *
   * @return An instance of the MovementTable.
   */
  def movementTable: MovementTable

  /**
   * Accessor for the bankAccount Type Table table.
   *
   * @return An instance of the BankAccountTypeTable.
   */
  def bankAccountTypeTable: BankAccountTypeTable

object Database:
  def apply(url: String): Database = TablesImpl(url)

  private case class TablesImpl(url: String) extends Database:
    private val connection: Connection = DriverManager.getConnection(url)
    private val personTab: PersonTable = PersonTable(connection, this)
    private val employeeTab: EmployeeTable = EmployeeTable(connection, this)
    private val customerTab: CustomerTable = CustomerTable(connection, this)
    private val appointmentTab: AppointmentTable = AppointmentTable(connection, this)
    private val bankAccountTab: BankAccountTable = BankAccountTable(connection, this)
    private val interestTab: InterestTable = InterestTable(connection, this)
    private val movementTab: MovementTable = MovementTable(connection, this)
    private val bankAccountTypeTab: BankAccountTypeTable = BankAccountTypeTable(connection, this)

    personTab.initialize()
    employeeTab.initialize()
    employeeTab.initialize()
    customerTab.initialize()
    appointmentTab.initialize()
    bankAccountTab.initialize()
    interestTab.initialize()
    movementTab.initialize()
    bankAccountTypeTab.initialize()

    override def personTable: PersonTable = personTab
    override def employeeTable: EmployeeTable = employeeTab
    override def customerTable: CustomerTable = customerTab
    override def appointmentTable: AppointmentTable = appointmentTab
    override def bankAccountTable: BankAccountTable = bankAccountTab
    override def interestTable : InterestTable = interestTab
    override def movementTable: MovementTable = movementTab
    override def bankAccountTypeTable: BankAccountTypeTable = bankAccountTypeTab













