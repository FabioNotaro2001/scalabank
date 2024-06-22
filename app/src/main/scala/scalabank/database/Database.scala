package scalabank.database

import scalabank.database.appointment.AppointmentTable
import scalabank.database.bank.BankAccountTable
import scalabank.database.customer.CustomerTable
import scalabank.database.employee.EmployeeTable
import scalabank.database.person.PersonTable
import scalabank.database.interest.*
import java.sql.{Connection, DriverManager}
import scalabank.database.interest

trait Database:
  def personTable: PersonTable
  def employeeTable: EmployeeTable
  def customerTable: CustomerTable
  def appointmentTable: AppointmentTable
  def bankAccountTable: BankAccountTable
  def interestTable: InterestTable

object Database:
  def apply(url: String): Database = TablesImpl(url)

  private case class TablesImpl(url: String) extends Database:
    private val connection: Connection = DriverManager.getConnection(url)
    private val personTab: PersonTable = PersonTable(connection)
    private val employeeTab: EmployeeTable = EmployeeTable(connection)
    private val customerTab: CustomerTable = CustomerTable(connection)
    private val appointmentTab: AppointmentTable = AppointmentTable(connection, customerTab, employeeTab)
    private val bankAccountTab: BankAccountTable = BankAccountTable(connection, customerTab)
    private val interestTab: InterestTable = InterestTable(connection)

    override def personTable: PersonTable = personTab
    override def employeeTable: EmployeeTable = employeeTab
    override def customerTable: CustomerTable = customerTab
    override def appointmentTable: AppointmentTable = appointmentTab
    override def bankAccountTable: BankAccountTable = bankAccountTab
    override def interestTable : InterestTable = interestTab















