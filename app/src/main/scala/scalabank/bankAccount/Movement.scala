package scalabank.bankAccount

import scalabank.currency.MoneyADT.Money

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Represents a financial movement in a bank account.
 */
trait Movement:

  /**
   * The amount of money involved in the movement.
   * @return the amount of money
   */
  def value: Money

  /**
   * The date and time of the movement.
   * @return the date and time of the movement
   */
  def date: LocalDateTime

  /**
   * The bank account receiving the movement.
   * @return the receiver bank account
   */
  def receiverBankAccount: BankAccount

  /**
   * The fee associated with the movement.
   * @return the fee
   */
  def fee: Money

  /**
   * The bank account sending the movement.
   * @return the sender bank account
   */
  def senderBankAccount: BankAccount

  /**
   * The date formatter for formatting the date and time of the movement.
   * @return the date formatter
   */
  def dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  /**
   * Executes the movement operation.
   * @return true if the operation is successful
   */
  def doOperation(): Boolean
