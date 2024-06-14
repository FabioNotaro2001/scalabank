package scalabank.loan

import scalabank.entities.*
import scalabank.loan.*

trait InterestManager:
  def getDefaultInterest: InterestRate
  def getInterestForYoungCustomer: InterestRate
  def getInterestForOldCutsomer: InterestRate
  def findAppropriateInterestForCustomer(customer: Customer): InterestRate

object InterestManager:
  def apply(): InterestManager = InterestManagerImpl()
  private class InterestManagerImpl() extends InterestManager:
    override def getDefaultInterest: InterestRate = InterestRate(0.04)
    override def getInterestForYoungCustomer: InterestRate = InterestRate(0.03)
    override def getInterestForOldCutsomer: InterestRate = InterestRate(0.05)

    //TODO: mettere il coso sotto in una classe iversa e separata.
    override def findAppropriateInterestForCustomer(customer: Customer): InterestRate = customer match
      case customer: YoungCustomer => getInterestForYoungCustomer
      //case customer.isOld => getInterestForOldCustomer  //TODO: far aggiungere a Mazzo isOld.
      case _ => getDefaultInterest