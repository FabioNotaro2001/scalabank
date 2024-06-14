package scalabank.loan

import scalabank.currency.MoneyADT.Money
import scalabank.entities.Customer

trait Loan:
  def customer: Customer
  def requiredAmount: Money
  def numberOfMonthlyPayments: Int
  def amountOfSinglePayment: Money
  def interestRate: Double
  def totalAmount: Money

// TODO Come migliorare formattazione di questo file sotto?
//TODO: Non sono troppi campi quelli dentro apply di Loan?????
// TODO: sono giusti i campi che sono soldi rappresentati come int e interestRate rappresentato come BigDecimal? Forse ci andrebbero nostre classi apposite?????
// TODO: InterestManager potrebbe anche gestire tassi variabili con una lista dei tassi.
// TODO: Pensa se cambiare Int in Money (magari aggiungendo a Money che non possono essere negativi).
// TODO: L'interesse creerei una nuova classe Interest, e due sottoclassi FixedInterest (che ha un double interest=0.04) o VariableInterest (che ha una lista interest[0.04, 0.05, 0.03]).
object Loan:
  def apply(client: Customer, requiredAmount: Money, numberOfMonthlyPayments: Int, interestRate: Double): Loan =
    LoanImpl(client, requiredAmount, numberOfMonthlyPayments, interestRate)

  private case class LoanImpl(override val customer: Customer, override val requiredAmount: Money,
                              override val numberOfMonthlyPayments: Int, override val interestRate: Double) extends Loan:
    override def totalAmount: Money =  requiredAmount * (1 + interestRate)
    override def amountOfSinglePayment: Money = totalAmount / numberOfMonthlyPayments