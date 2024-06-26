package scalabank.bankAccount

import scalabank.currency.{CurrencyConverter, FeeManager}
import scalabank.currency.MoneyADT.Money
import java.time.LocalDateTime

/**
 * Represents a money transfer movement between two bank accounts.
 *
 * @constructor Creates a new money transfer with the specified sender and receiver bank accounts, and transfer value.
 * @param senderBankAccount the bank account from which the money is transferred.
 * @param receiverBankAccount the bank account to which the money is transferred.
 * @param value the amount of money being transferred.
 */
case class MoneyTransfer(override val senderBankAccount: BankAccount, override val receiverBankAccount: BankAccount, var value: Money, override val date: LocalDateTime = LocalDateTime.now()) extends Movement:

  /**
   * Provides a string representation of the money transfer.
   *
   * @return a string describing the money transfer.
   */
  override def toString: String = s"Money transfer of $value at ${date.format(super.dateFormatter)} between bank account ${senderBankAccount.id} and ${receiverBankAccount.id}"

  /**
   * Executes the money transfer operation.
   *
   * Deducts the transfer value plus the fee from the sender bank account's balance and adds the transfer value to the receiver bank account's balance.
   *
   * @return true if the operation is successful, false otherwise.
   */
  override def doOperation(): Boolean =
    val feePerMoneyTransfer = senderBankAccount.bankAccountType.feePerOperation
    val amountWithFee = FeeManager.calculateAmountWithFee(value, feePerMoneyTransfer)
    val currencyOfSender = senderBankAccount.currency
    val currencyOfReceiver = receiverBankAccount.currency

    if currencyOfSender.code != currencyOfReceiver.code then
      val converter = CurrencyConverter()
      value = converter.convert(value, currencyOfSender, currencyOfReceiver)
    if senderBankAccount.balance >= amountWithFee then
      senderBankAccount.setBalance(senderBankAccount.balance - amountWithFee)
      receiverBankAccount.setBalance(receiverBankAccount.balance + value)
      true
    else false
