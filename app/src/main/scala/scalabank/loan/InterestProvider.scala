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
  /**
   * Factory method to create a new InterestProvider instance.
   *
   * @return a new instance of InterestProvider.
   */
  def apply(): InterestProvider = new InterestProviderImpl()

  /**
   * Private implementation of the InterestProvider trait.
   */
  private class InterestProviderImpl() extends InterestProvider:
    /**
     * Retrieves the default interest rate.
     *
     * @return the default InterestRate.
     */
    override def getDefaultInterest: InterestRate = InterestRate(0.04)

    /**
     * Retrieves the interest rate for young customers.
     *
     * @return the InterestRate for young customers.
     */
    override def getInterestForYoungCustomer: InterestRate = InterestRate(0.03)

    /**
     * Retrieves the interest rate for old customers.
     *
     * @return the InterestRate for old customers.
     */
    override def getInterestForOldCustomer: InterestRate = InterestRate(0.05)