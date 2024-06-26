package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.bankAccount.{Deposit, MoneyTransfer, Withdraw}
import scalabank.currency.MoneyADT.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(classOf[JUnitRunner])
class MovementTest extends AnyFlatSpec with Matchers:
  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
  private val database = Database("jdbc:h2:mem:test29;DB_CLOSE_DELAY=-1")

  import database.*

  private def convertDateInFuture(days: Int): LocalDateTime =
    val date = LocalDateTime.now.plusDays(days)
    LocalDateTime.parse(date.format(dateFormat), dateFormat)

  "MovementTable" should "insert and retrieve a deposit correctly" in:
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val deposit = Deposit(receiverAccount, 100.toMoney, convertDateInFuture(1))
    movementTable.insert(deposit)
    val id = (receiverAccount.id, deposit.date)
    val retrievedMovement = movementTable.findById(id)
    retrievedMovement.get shouldBe deposit

  it should "insert and retrieve a withdraw correctly" in:
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val withdraw = Withdraw(receiverAccount, 50.toMoney, convertDateInFuture(2))
    movementTable.insert(withdraw)
    val id = (receiverAccount.id, withdraw.date)
    val retrievedMovement = movementTable.findById(id)
    retrievedMovement.get shouldBe withdraw

  it should "insert and retrieve a money transfer correctly" in:
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val senderAccount = accounts(1)
    val transfer = MoneyTransfer(senderAccount, receiverAccount, 75.toMoney, convertDateInFuture(3))
    movementTable.insert(transfer)
    val id = (receiverAccount.id, transfer.date)
    val retrievedMovement = movementTable.findById(id)
    retrievedMovement.get shouldBe transfer

  it should "update a movement correctly" in:
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val deposit = Deposit(receiverAccount, 100.toMoney, convertDateInFuture(4))
    movementTable.insert(deposit)
    val id = (receiverAccount.id, deposit.date)
    val updatedDeposit = Deposit(receiverAccount, 150.toMoney, deposit.date)
    movementTable.update(updatedDeposit)
    val retrievedMovement = movementTable.findById(id)
    retrievedMovement.get shouldBe updatedDeposit

  it should "delete a movement correctly" in:
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val withdraw = Withdraw(receiverAccount, 50.toMoney, convertDateInFuture(5))
    movementTable.insert(withdraw)
    val id = (receiverAccount.id, withdraw.date)
    movementTable.delete(id)
    val retrievedMovement = movementTable.findById(id)
    retrievedMovement shouldBe None

  it should "find all movements correctly" in:
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val senderAccount = accounts(1)
    val movements = Seq(
      Deposit(receiverAccount, 100.toMoney, convertDateInFuture(6)),
      Withdraw(receiverAccount, 50.toMoney, convertDateInFuture(7)),
      MoneyTransfer(senderAccount, receiverAccount, 75.toMoney, convertDateInFuture(8))
    )
    movements.foreach(movementTable.insert)
    val retrievedMovements = movementTable.findAll()
    retrievedMovements should contain allElementsOf movements

  it should "find movements by bank account correctly" in :
    val accounts = bankAccountTable.findAll()
    val receiverAccount = accounts.head
    val senderAccount = accounts(1)
    val movements = Seq(
      Deposit(receiverAccount, 100.toMoney, convertDateInFuture(9)),
      Withdraw(receiverAccount, 50.toMoney, convertDateInFuture(10)),
      MoneyTransfer(senderAccount, receiverAccount, 75.toMoney, convertDateInFuture(11)),
      MoneyTransfer(receiverAccount, senderAccount, 80.toMoney, convertDateInFuture(12))
    )
    movements.foreach(movementTable.insert)
    val retrievedMovementsReceiver = movementTable.findByBankAccount(receiverAccount.id)
    val retrievedMovementsSender = movementTable.findByBankAccount(senderAccount.id)
    retrievedMovementsReceiver should contain allElementsOf movements.take(3)
    retrievedMovementsSender should contain allElementsOf Seq(movements(2), movements(3))