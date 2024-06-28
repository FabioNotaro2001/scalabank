package scalabank.loan

import scalabank.entities.*
import scalabank.entities.Customer.OldCustomerImpl
import scalabank.loan.*

/**
 * Manages the determination of appropriate interest rates for customers.
 */
trait InterestManager:
  /**
   * Finds the appropriate interest rate for a given customer.
   *
   * @param customer the customer for whom to find the interest rate.
   * @return the InterestRate deemed appropriate for the customer.
   */
  def findAppropriateInterestForCustomer(customer: Customer): InterestRate

/**
 * Companion object for the InterestManager trait.
 */
object InterestManager:
  /**
   * Factory method to create a new InterestManager instance.
   *
   * @return a new instance of InterestManager.
   */
  def apply(): InterestManager = new InterestManagerImpl()

  /**
   * Private implementation of the InterestManager trait.
   * Utilizes InterestProvider to determine the appropriate interest rate.
   */
  private class InterestManagerImpl extends InterestManager:
    private val interestProvider = InterestProvider()

    /**
     * Finds the appropriate interest rate for a given customer based on their category.
     *
     * @param customer the customer for whom to find the interest rate.
     * @return the InterestRate deemed appropriate for the customer.
     */
    override def findAppropriateInterestForCustomer(customer: Customer): InterestRate = customer match
      case customer: CustomerComponent#YoungCustomerImpl => interestProvider.getInterestForYoungCustomer
      case customer: CustomerComponent#OldCustomerImpl => interestProvider.getInterestForOldCustomer
      case _ => interestProvider.getDefaultInterest
