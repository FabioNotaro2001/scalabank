package scalabank.database.movement

import scalabank.bankAccount.{BankAccount, Deposit, MoneyTransfer, Movement, Withdraw}
import scalabank.currency.MoneyADT.*
import scalabank.database.{Database, DatabaseOperations}

import java.sql.{Connection, PreparedStatement, ResultSet, Timestamp}
import scala.collection.mutable.Map as MutableMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Class representing the movements table in the database.
 *
 * @param connection The database connection to use.
 * @param database The database reference.
 */
class MovementTable(override val connection: Connection, override val database: Database) extends DatabaseOperations[Movement, (Int, LocalDateTime)]:
  import database.*

  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val fetchedMovements = MutableMap[(Int, LocalDateTime), Movement]()

  private val tableCreated =
    if !tableExists("movements", connection) then
      val query = "CREATE TABLE IF NOT EXISTS movements (" +
        "receiverBankAccountId INT, date DATETIME, type VARCHAR(20), amount VARCHAR(30), " +
        "senderBankAccountId INT, " +
        "PRIMARY KEY (receiverBankAccountId, date))"
      connection.createStatement.execute(query)
      true
    else false

  override def initialize(): Unit =
    if tableCreated then
      populateDB()

  private def checkMovement(entity: Movement, stmt: PreparedStatement, enumeration: Int): Unit =
    entity match
      case transfer: MoneyTransfer =>
        stmt.setInt(enumeration, transfer.senderBankAccount.id)
      case withdraw: Withdraw =>
        stmt.setInt(enumeration, 0)
      case _ =>
        stmt.setInt(enumeration, 0)

  def insert(entity: Movement): Unit =
    val query = "INSERT INTO movements (receiverBankAccountId, date, type, amount, senderBankAccountId) VALUES (?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, entity.receiverBankAccount.id)
    stmt.setString(2, entity.date.format(dateFormat))
    stmt.setString(3, entity.getClass.getSimpleName)
    stmt.setString(4, entity.value.toString)
    checkMovement(entity, stmt, 5)
    stmt.executeUpdate()

  private def createMovement(resultSet: ResultSet): Movement =
    val receiverBankAccountId = resultSet.getInt("receiverBankAccountId")
    val date = resultSet.getTimestamp("date").toLocalDateTime
    fetchedMovements.get((receiverBankAccountId, date)) match
      case Some(m) => m
      case None =>
        val movementType = resultSet.getString("type")
        val value = resultSet.getString("amount").toMoney
        val receiverBankAccount = bankAccountTable.findById(receiverBankAccountId).get
        val movement = movementType match
          case "Deposit" =>
            Deposit(receiverBankAccount, value, date)
          case "Withdraw" =>
            Withdraw(receiverBankAccount, value, date)
          case "MoneyTransfer" =>
            val senderBankAccountId = resultSet.getInt("senderBankAccountId")
            val senderBankAccount = bankAccountTable.findById(senderBankAccountId).get
            MoneyTransfer(senderBankAccount, receiverBankAccount, value, date)
        fetchedMovements.put((receiverBankAccountId, date), movement)
        movement

  def findById(id: (Int, LocalDateTime)): Option[Movement] =
    val query = "SELECT * FROM movements WHERE receiverBankAccountId = ? AND date = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id._1)
    stmt.setTimestamp(2, Timestamp.valueOf(id._2))
    val result = stmt.executeQuery
    if (result.next) Some(createMovement(result)) else None

  /**
   * Finds movements associated with a bank account identified by the given ID.
   *
   * @param id The ID of the bank account to search movements for.
   * @return A sequence of Movement objects corresponding to the movements found.
   */
  def findByBankAccount(id: Int): Seq[Movement] =
    val query = "SELECT * FROM movements WHERE receiverBankAccountId = ? OR senderBankAccountId = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    stmt.setInt(2, id)
    val resultSet = stmt.executeQuery(query)
    toIterator(resultSet)

  private def toIterator(resultSet: ResultSet): Seq[Movement] =
    new Iterator[Movement]:
      def hasNext: Boolean = resultSet.next()
      def next(): Movement = createMovement(resultSet)
    .toSeq

  def findAll(): Seq[Movement] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM movements"
    val resultSet = stmt.executeQuery(query)
    toIterator(resultSet)

  def update(entity: Movement): Unit =
    val query = "UPDATE movements SET type = ?, amount = ?, senderBankAccountId = ? WHERE receiverBankAccountId = ? AND date = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.getClass.getSimpleName)
    stmt.setString(2, entity.value.toString)
    checkMovement(entity, stmt, 3)
    stmt.setInt(4, entity.receiverBankAccount.id)
    stmt.setTimestamp(5, Timestamp.valueOf(entity.date))
    stmt.executeUpdate()
    fetchedMovements.remove((entity.receiverBankAccount.id, entity.date))

  def delete(id: (Int, LocalDateTime)): Unit =
    val query = "DELETE FROM movements WHERE receiverBankAccountId = ? AND date = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id._1)
    stmt.setTimestamp(2, Timestamp.valueOf(id._2))
    stmt.executeUpdate()
    fetchedMovements.remove(id)

  private def populateDB(): Unit =
    val bankAccounts = database.bankAccountTable.findAll()
    val movements = for
      receiverBankAccount <- bankAccounts
      i <- 1 to 3
      movement <- Seq(
        Deposit(receiverBankAccount, 100.toMoney, LocalDateTime.now.plusDays(i).plusSeconds(i)),
        Withdraw(receiverBankAccount, 50.toMoney, LocalDateTime.now.plusDays(i + 1).plusSeconds(i)),
        if bankAccounts.length > 1 then
          val senderBankAccount = bankAccounts((bankAccounts.indexOf(receiverBankAccount) + 1) % bankAccounts.length)
          MoneyTransfer(senderBankAccount, receiverBankAccount, 75.toMoney, LocalDateTime.now.plusDays(i + 2).plusSeconds(i))
        else null
      ) if movement != null
    yield movement
    movements.foreach(insert)
