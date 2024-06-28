package scalabank.logger

import scalabank.utils.TimeFormatter

/**
 * Trait defining the functionality for formatting log message prefixes.
 */
trait PrefixFormatter:
  /**
   * Retrieves the prefix with the current time.
   *
   * @return a string representing the prefix, including the current time.
   */
  def getPrefixWithCurrentTime: String

  /**
   * Retrieves the creation prefix.
   *
   * @return a string representing the creation prefix.
   */
  def getCreationPrefix: String

  /**
   * Retrieves the loan simulation prefix.
   *
   * @return a string representing the loan simulation prefix.
   */
  def getLoanSimulationPrefix: String

  /**
   * Retrieves the bank account opening prefix.
   *
   * @return a string representing the bank account opening prefix.
   */
  def getPrefixForBankAccountOpening: String

  /**
   * Retrieves the deposit prefix.
   *
   * @return a string representing the deposit prefix.
   */
  def getPrefixForDeposit: String

  /**
   * Retrieves the withdraw prefix.
   *
   * @return a string representing the withdraw prefix.
   */
  def getPrefixForWithdraw: String

  /**
   * Retrieves the money transfer prefix.
   *
   * @return a string representing the money transfer prefix.
   */
  def getPrefixForMoneyTransfer: String

/**
 * Companion object for the PrefixFormatter trait.
 */
object PrefixFormatter:
  /**
   * Instantiates a new PrefixFormatter.
   *
   * @return a new instance of PrefixFormatter.
   */
  def apply(): PrefixFormatter = new PrefixFormatterImpl()

  /**
   * Private class implementing the PrefixFormatter trait.
   */
  private class PrefixFormatterImpl extends PrefixFormatter:
    private val timeFormatter = TimeFormatter()

    /**
     * Retrieves the prefix with the current time.
     *
     * @return a string representing the prefix, including the current time.
     */
    override def getPrefixWithCurrentTime: String = s"[${timeFormatter.getTimeFormatted}] "

    /**
     * Retrieves the creation prefix.
     *
     * @return a string representing the creation prefix.
     */
    override def getCreationPrefix: String = "[CREATION] "

    /**
     * Retrieves the loan simulation prefix.
     *
     * @return a string representing the loan simulation prefix.
     */
    override def getLoanSimulationPrefix: String = "[LOAN SIMULATION] "

    /**
     * Retrieves the bank account opening prefix.
     *
     * @return a string representing the bank account opening prefix.
     */
    override def getPrefixForBankAccountOpening: String = "[BANK ACCOUNT OPENING] "

    /**
     * Retrieves the deposit prefix.
     *
     * @return a string representing the deposit prefix.
     */
    override def getPrefixForDeposit: String = "[BANK ACCOUNT DEPOSIT] "

    /**
     * Retrieves the withdraw prefix.
     *
     * @return a string representing the withdraw prefix.
     */
    override def getPrefixForWithdraw: String = "[BANK ACCOUNT WITHDRAW] "

    /**
     * Retrieves the money transfer prefix.
     *
     * @return a string representing the money transfer prefix.
     */
    override def getPrefixForMoneyTransfer: String = "[BANK ACCOUNT MONEY TRANSFER] "
