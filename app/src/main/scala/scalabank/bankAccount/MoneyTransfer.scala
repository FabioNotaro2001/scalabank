package scalabank.bankAccount

import scalabank.currency.{CurrencyConverter, FeeManager}
import scalabank.currency.MoneyADT.Money
import java.time.LocalDateTime

/**
 * Represents a money transfer movement between two bank accounts.
 *
 * @constructor Creates a new money transfer with the specified sender and receiver bank accounts, transfer value, and fee.
 * @param senderBankAccount the bank account from which the money is transferred.
 * @param receiverBankAccount the bank account to which the money is transferred.
 * @param value the amount of money being transferred.
 * @param fee the fee applied to the money transfer.
 * @param date the date and time when the money transfer is created, defaults to the current date and time.
 */
case class MoneyTransfer(
                          override val senderBankAccount: BankAccount,
                          override val receiverBankAccount: BankAccount,
                          var value: Money,
                          override val fee: Money,
                          override val date: LocalDateTime = LocalDateTime.now()
                        ) extends Movement {

  /**
   * Provides a string representation of the money transfer.
   *
   * @return a string describing the money transfer, including the date, transfer amount, and involved bank account IDs.
   */
  override def toString: String =
    s"[${date.format(super.dateFormatter)}] Money transfer of $value between bank account ${senderBankAccount.id} and ${receiverBankAccount.id}"

  /**
   * Executes the money transfer operation.
   *
   * Deducts the transfer value plus the fee from the sender bank account's balance and adds the transfer value to the receiver bank account's balance.
   * If the sender's and receiver's currencies differ, the transfer value is converted using the currency converter.
   *
   * @return true if the operation is successful (i.e., the sender has enough balance), false otherwise.
   */
  override def doOperation(): Boolean = {
    val amountWithFee = FeeManager.calculateAmountWithFee(value, fee)
    val currencyOfSender = senderBankAccount.currency
    val currencyOfReceiver = receiverBankAccount.currency

    if (currencyOfSender.code != currencyOfReceiver.code) {
      val converter = CurrencyConverter()
      value = converter.convert(value, currencyOfSender, currencyOfReceiver)
    }

    if (senderBankAccount.balance >= amountWithFee) {
      senderBankAccount.setBalance(senderBankAccount.balance - amountWithFee)
      receiverBankAccount.setBalance(receiverBankAccount.balance + value)
      true
    } else {
      false
    }
  }
}
