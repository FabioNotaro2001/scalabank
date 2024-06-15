package scalabank.loan

import scalabank.loan.*

trait InterestProvider:
  def getDefaultInterest: InterestRate
  def getInterestForYoungCustomer: InterestRate
  def getInterestForOldCutsomer: InterestRate

object InterestProvider:
  def apply(): InterestProvider = InterestProviderImpl()
  private class InterestProviderImpl() extends InterestProvider:
    override def getDefaultInterest: InterestRate = InterestRate(0.04)  // TODO: Pensare se questo valore può essere preso tramite API.
    override def getInterestForYoungCustomer: InterestRate = InterestRate(0.03)
    override def getInterestForOldCutsomer: InterestRate = InterestRate(0.05)
