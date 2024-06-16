package scalabank.loan

import scalabank.loan.*

trait InterestProvider:
  def getDefaultInterest: InterestRate
  def getInterestForYoungCustomer: InterestRate
  def getInterestForOldCustomer: InterestRate

object InterestProvider:
  def apply(): InterestProvider = InterestProviderImpl()
  private class InterestProviderImpl() extends InterestProvider:
    override def getDefaultInterest: InterestRate = InterestRate(0.04)  // TODO: Pensare se questo valore può essere preso tramite API. In alternativa si potrebbe pensare di prenderlo da database prolog.
    override def getInterestForYoungCustomer: InterestRate = InterestRate(0.03)
    override def getInterestForOldCustomer: InterestRate = InterestRate(0.05)