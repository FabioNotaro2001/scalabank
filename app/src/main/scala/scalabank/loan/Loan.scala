package scalabank.loan

import scalabank.entities.Customer

trait Loan:
  def customer: Customer
  def requiredAmount: Double
  def numberOfMonthlyPayments: Int
  def amountOfSinglePayment: Double
  def interestRate: Double
  def totalAmount: Double

// TODO Come migliorare formattazione di questo file sotto?
//TODO: Non sono troppi campi quelli dentro apply di Loan?????
// TODO: sono giusti i campi che sono soldi rappresentati come int e interestRate rappresentato come BigDecimal? Forse ci andrebbero nostre classi apposite?????
//TODO: InterestManager potrebbe anche gestire tassi variabili con una lista dei tassi.
// TODO: Pensa se cambiare Int in Money (magari aggiungendo a Money che non possono essere negativi).
// TODO: L'interesse va bene double o è meglio in un qualche tipo percentuale da me creato?
object Loan:
  def apply(client: Customer, requiredAmount: Double, numberOfMonthlyPayments: Int, interestRate: Double): Loan =
    LoanImpl(client, requiredAmount, numberOfMonthlyPayments, interestRate)

  private case class LoanImpl(override val customer: Customer, override val requiredAmount: Double,
                              override val numberOfMonthlyPayments: Int, override val interestRate: Double) extends Loan:
    override def totalAmount: Double =  requiredAmount * (1 + interestRate)
    override def amountOfSinglePayment: Double = totalAmount / numberOfMonthlyPayments