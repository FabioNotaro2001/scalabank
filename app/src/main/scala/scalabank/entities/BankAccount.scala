package scalabank.entities

import scalabank.currency.MoneyADT.Money
import scalabank.currency.MoneyADT
import scalabank.currency.Currency


enum StateBankAccount:
    case Active, Disactive, Closed

trait BankAccount:
    def id: Int
    def balance: Money
    def currency: Currency
    def state: StateBankAccount
    def computeFee: Money
    def computeInterest: Money

case class BaseBankAccountImpl(val _id: Int,
                      val _balance: Money,
                      val _currency: Currency,
                      val _state: StateBankAccount) extends BankAccount:

    override def id: Int = _id

    override def balance: Money = _balance

    override def currency: Currency = _currency

    override def state: StateBankAccount = _state

    override def computeFee: Money = ???

    override def computeInterest: Money = ???

object BaseBankAccount:
    def apply(id: Int, balance: Money, currency: Currency, state: StateBankAccount) = BaseBankAccountImpl(id, balance, currency, state)

case class SuperBankAccountImpl(val _id: Int,
                               val _balance: Money,
                               val _currency: Currency,
                               val _state: StateBankAccount) extends BankAccount:

    override def id: Int = _id

    override def balance: Money = _balance

    override def currency: Currency = _currency

    override def state: StateBankAccount = _state

    override def computeFee: Money = ???

    override def computeInterest: Money = ???

object SuperBankAccount:
    def apply(id: Int, balance: Money, currency: Currency, state: StateBankAccount) = SuperBankAccountImpl(id, balance, currency, state)
