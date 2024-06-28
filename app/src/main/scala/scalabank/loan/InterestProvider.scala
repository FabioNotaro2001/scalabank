package scalabank.loan

import scalabank.loan.*

/**
 * Provides default interest rates and rates based on specified parameters that must be take into account.
 */
trait InterestProvider:
  /**
   * Retrieves the default interest rate.
   *
   * @return the default InterestRate.
   */
  def getDefaultInterest: InterestRate

  /**
   * Retrieves the interest rate for young customers.
   *
   * @return the InterestRate for young customers.
   */
  def getInterestForYoungCustomer: InterestRate

  /**
   * Retrieves the interest rate for old customers.
   *
   * @return the InterestRate for old customers.
   */
  def getInterestForOldCustomer: InterestRate

/**
 * Companion object for the InterestProvider trait.
 */
object InterestProvider:
  private var _interestValues: Map[String, InterestRate] = Map()
  def setInterestValues(interestValues: Map[String, InterestRate]): Unit =
    require(interestValues.contains("default") && interestValues.contains("young") && interestValues.contains("old"))
    _interestValues = interestValues

  /**
   * Factory method to create a new InterestProvider instance.
   *
   * @return a new instance of InterestProvider.
   */
  def apply(): InterestProvider = InterestProviderImpl(_interestValues)

  /**
   * Private implementation of the InterestProvider trait.
   */
  private class InterestProviderImpl(interestValues: Map[String, InterestRate]) extends InterestProvider:
    require(interestValues.contains("default") && interestValues.contains("young") && interestValues.contains("old"))
    /**
     * Retrieves the default interest rate.
     *
     * @return the default InterestRate.
     */
    override def getDefaultInterest: InterestRate = interestValues("default")

    /**
     * Retrieves the interest rate for young customers.
     *
     * @return the InterestRate for young customers.
     */
    override def getInterestForYoungCustomer: InterestRate = interestValues("young")

    /**
     * Retrieves the interest rate for old customers.
     *
     * @return the InterestRate for old customers.
     */
    override def getInterestForOldCustomer: InterestRate = interestValues("old")