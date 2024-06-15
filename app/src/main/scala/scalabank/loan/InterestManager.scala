package scalabank.loan

import scalabank.entities.*
import scalabank.loan.*

trait InterestManager:
  def findAppropriateInterestForCustomer(customer: Customer): InterestRate

object InterestManager:
  def apply(): InterestManager = InterestManagerImpl()
  private class InterestManagerImpl() extends InterestManager:
    private val interestProvider = InterestProvider()
    override def findAppropriateInterestForCustomer(customer: Customer): InterestRate = customer match
      case customer: YoungCustomer => interestProvider.getInterestForYoungCustomer
      //case customer.isOld => getInterestForOldCustomer  //TODO: far aggiungere a Mazzo isOld.
      case _ => interestProvider.getDefaultInterest