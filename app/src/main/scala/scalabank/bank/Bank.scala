package scalabank.bank

import scalabank.appointment.Appointment
import scalabank.bank
import scalabank.currency.Currency
import scalabank.currency.MoneyADT.Money
import scalabank.entities.Customer.logger
import scalabank.entities.StateBankAccount.Active
import scalabank.entities.{BankAccount, Customer, Employee}
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}
import scalabank.currency.MoneyADT.toMoney

import java.time.LocalDateTime
import scala.annotation.targetName
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.{HashMap as MutableHashMap, Map as MutableMap}
import scala.util.Random

extension [A](opt: Option[A])
  @targetName("getOrElse")
  def ??(value: A): A = opt.getOrElse(value)

/**
 * Empty trait for representing information regarding a bank
 */
trait BankInformation

trait BankAccountType:
  def create: (Int, Currency, Customer) => BankAccount

enum BankAccountTypes(override val create: (Int, Currency, Customer) => BankAccount) extends BankAccountType:
  case BaseAccount extends BankAccountTypes(
    (id: Int, currency: Currency, customer: Customer) => BankAccount(id, customer, 0.toMoney, currency, Active)
  )

/**
 * Trait for representing a bank, which can have customers, employees and appointments
 */
trait Bank:
  /**
   * @return the information associayed to the bank
   */
  def bankInformation: BankInformation

  /**
   * Attempts to login as customer
   * @param cf the customer's CF
   * @return the customer if the login is successful
   */
  def customerLogin(cf: String): Option[Customer]

  /**
   * Attempts to login as employee
   * @param cf the employee's CF
   * @return the employee if the login is successful
   */
  def employeeLogin(cf: String): Option[Employee]

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
  def createAppointment(customer: Customer, description: String, date: LocalDateTime, duration: Int): Appointment

  /**
   * Updates an appointment
   * @param appointment the appointment to be modified
   * @param description the new description, if changed
   * @param date the new date, if changed
   * @param duration the new duration, if changed
   * @return the updated appointment
   */
  def updateAppointment(appointment: Appointment, description: Option[String], date: Option[LocalDateTime], duration: Option[Int]): Appointment

  /**
   * Deletes an appointment
   * @param appointment the appointment to be deleted
   */
  def cancelAppointment(appointment: Appointment): Unit

  /**
   * Creates a new bank account for the customer
   * @param customer the customer
   * @param bankAccountType the type of bank account
   * @param currency the currency of the bank account
   * @return the created bank account
   */
  def addBankAccount(customer: Customer, bankAccountType: BankAccountType, currency: Currency): BankAccount


abstract class AbstractBankImpl[T <: BankInformation](override val bankInformation: T) extends Bank:
  protected val employees: ListBuffer[Employee] = ListBuffer()
  protected val customers: ListBuffer[Customer] = ListBuffer()

  override def customerLogin(cf: String): Option[Customer] =
    customers.find(_.cf == cf)

  override def employeeLogin(cf: String): Option[Employee] =
    employees.find(_.cf == cf)

  override def addEmployee(employee: Employee): Unit =
    employees.addOne(employee)

  override def addCustomer(customer: Customer): Unit =
    customers.addOne(customer)

  override def addBankAccount(customer: Customer, bankAccountType: BankAccountType, currency: Currency): BankAccount =
    val acc = bankAccountType.create(LocalDateTime.now.getNano, currency, customer)
    //customer.addBankAccount(acc)
    acc


trait BankComponent:
  loggerDependency: LoggerDependency =>
  case class PhysicalBankInformation(name: String, address: String, phoneNumber: String) extends BankInformation
  case class PhysicalBank(bankInfo: PhysicalBankInformation) extends AbstractBankImpl[PhysicalBankInformation](bankInfo):
    private val appointments: MutableMap[Customer, ListBuffer[Appointment]] = MutableHashMap()
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)

    override def createAppointment(customer: Customer, description: String, date: LocalDateTime, duration: Int): Appointment =
      require(customers.contains(customer), "Customer not registered with the bank")
      assume(employees.nonEmpty)
      val employee = employees(Random().nextInt(employees.length))
      val appointment = Appointment(customer, employee, description, date, duration)
      if appointments.contains(customer)
      then
        appointments(customer).addOne(appointment)
      else
        appointments.put(customer, ListBuffer(appointment))
      customer.addAppointment(appointment)
      employee.addAppointment(appointment)
      appointment

    private def verifyAppointmentRequirements(appointment: Appointment): Unit =
      require(appointment != null, "The appointment must be defined")
      require(appointment.date.isAfter(LocalDateTime.now), "Cannot cancel an appointment that has already passed")
      require(customers.contains(appointment.customer), "Customer not registered with the bank")
      require(employees.contains(appointment.employee), "Employee not affiliated with the bank")

    override def updateAppointment(appointment: Appointment, description: Option[String], date: Option[LocalDateTime], duration: Option[Int]): Appointment =
      verifyAppointmentRequirements(appointment)
      require(description.nonEmpty || date.nonEmpty || duration.nonEmpty, "At least one of the fields of the appointment must be updated")
      val newAppointment = Appointment(appointment.customer, appointment.employee,
                                        description ?? appointment.description, date ?? appointment.date, duration ?? appointment.duration)
      appointments.get(appointment.customer) match
        case Some(customerAppointments) =>
          val index = customerAppointments.indexOf(appointment)
          require(index != -1, "Customer has no such appointment registered")
          customerAppointments.remove(index)
          customerAppointments.insert(index, newAppointment)
          appointment.customer.updateAppointment(appointment)(newAppointment)
          appointment.employee.updateAppointment(appointment)(newAppointment)
          newAppointment
        case None =>
          throw IllegalArgumentException("Customer has no appointments registered")

    override def cancelAppointment(appointment: Appointment): Unit =
      verifyAppointmentRequirements(appointment)
      appointments.get(appointment.customer) match
        case Some(customerAppointments) =>
          val index = customerAppointments.indexOf(appointment)
          assert(index != -1, "Customer has no such appointment registered")
          customerAppointments.remove(index)
          appointment.customer.removeAppointment(appointment)
          appointment.employee.removeAppointment(appointment)
        case None =>
          throw AssertionError("Customer has no appointments registered")

  case class VirtualBankInformation(name: String, phoneNumber: String) extends BankInformation
  case class VirtualBank(bankInfo: VirtualBankInformation) extends AbstractBankImpl[VirtualBankInformation](bankInfo):
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)

    override def createAppointment(customer: Customer, description: String, date: LocalDateTime, duration: Int): Appointment =
      throw UnsupportedOperationException("Virtual banks don't support appointments")

    override def updateAppointment(appointment: Appointment, description: Option[String], date: Option[LocalDateTime], duration: Option[Int]): Appointment =
      throw UnsupportedOperationException("Virtual banks don't support appointments")

    override def cancelAppointment(appointment: Appointment): Unit =
      throw UnsupportedOperationException("Virtual banks don't support appointments")

object Bank extends LoggerDependency with BankComponent:
  override val logger: Logger = LoggerImpl()

  def physicalBank(name: String, address: String, phoneNumber: String): Bank = PhysicalBank(PhysicalBankInformation(name, address, phoneNumber))

  def virtualBank(name: String, phoneNumber: String): Bank = VirtualBank(VirtualBankInformation(name, phoneNumber))