package scalabank.bank

import scalabank.appointment.Appointment
import scalabank.bank
import scalabank.entities.{Customer, Employee}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

import java.time.LocalDateTime
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.{HashMap as MutableHashMap, Map as MutableMap}
import scala.util.Random

trait BankInformation

trait Bank:
  def bankInformation: BankInformation
  /**
   * Adds an employee to the bank
   * @param employee the employee to be added
   */
  def addEmployee(employee: Employee): Unit

  /**
   * Adds a customer to the bank
   * @param customer the customer to be added
   */
  def addCustomer(customer: Customer): Unit

  /**
   * Creates a new appointment for a customer
   * @param customer the customer requesting the appointment
   * @return a new appointment for the customer
   */
  def createAppointment(customer: Customer, description: String, date: LocalDateTime): Appointment

abstract class AbstractBankImpl[T <: BankInformation](override val bankInformation: T) extends Bank:
  protected val employees: ListBuffer[Employee] = ListBuffer()
  protected val customers: ListBuffer[Customer] = ListBuffer()

  override def addEmployee(employee: Employee): Unit =
    employees.addOne(employee)
  // TODO: impostare la banca nel dipendente

  override def addCustomer(customer: Customer): Unit =
    customers.addOne(customer)
// TODO: impostare la banca nel cliente

trait BankComponent:
  loggerDependency: LoggerDependency =>
  case class PhysicalBankInformation(name: String, address: String, phoneNumber: String) extends BankInformation

  case class PhysicalBank(override val bankInformation: PhysicalBankInformation) extends AbstractBankImpl[PhysicalBankInformation](bankInformation):
    private val appointments: MutableMap[Customer, ListBuffer[Appointment]] = MutableHashMap()

    override def createAppointment(customer: Customer, description: String, date: LocalDateTime): Appointment =
      require(date.isAfter(LocalDateTime.now), "Date of appointment has to be in the future")
      val employee = employees(Random().nextInt(employees.length))
      val appointment = Appointment(customer, employee, description, date)
      if appointments.contains(customer)
      then
        appointments(customer).addOne(appointment)
      else
        appointments.put(customer, ListBuffer(appointment))
      customer.addAppointment(appointment)
      employee.addAppointment(appointment)
      appointment

  case class VirtualBankInformation(name: String, phoneNumber: String) extends BankInformation
  case class VirtualBank(override val bankInformation: VirtualBankInformation) extends AbstractBankImpl[VirtualBankInformation](bankInformation):
    override def createAppointment(customer: Customer, description: String, date: LocalDateTime): Appointment =
      throw UnsupportedOperationException("Virtual banks don't support appointments")

object Bank extends LoggerDependency with BankComponent:
  override val logger: Logger = LoggerImpl()
  def physicalBank(name: String, address: String, phoneNumber: String): Bank = PhysicalBank(PhysicalBankInformation(name, address, phoneNumber))
  def virtualBank(name: String, phoneNumber: String): Bank = VirtualBank(VirtualBankInformation(name, phoneNumber))