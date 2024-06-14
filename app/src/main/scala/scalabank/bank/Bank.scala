package scalabank.bank

import scalabank.entities.{Customer, Employee}

import java.sql.Date

case class Appointment(client: Customer, employee: Employee, date: Date)

trait Bank:
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
  def createAppointment(customer: Customer): Appointment




