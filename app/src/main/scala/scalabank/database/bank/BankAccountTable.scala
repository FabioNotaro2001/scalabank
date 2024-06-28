package scalabank.database.bank

import scalabank.currency.Currency
import scalabank.database.{AbstractCache, Database, DatabaseOperations}
import scalabank.currency.MoneyADT.*
import scalabank.bankAccount.{BankAccount, StateBankAccount}

import java.sql.{Connection, ResultSet}
import scala.util.Random

/**
 * Class representing the bank account table in the database.
 *
 * @param connection    The database connection to use.
 * @param database      The database reference.
 */
class BankAccountTable(override val connection: Connection, override val database: Database) extends AbstractCache[BankAccount, Int] with DatabaseOperations[BankAccount, Int]:
  import database.*

  private val fetchedBankAccounts = cache

  private val tableCreatedNow =
    if !tableExists("bankAccount", connection) then
      val query = "CREATE TABLE IF NOT EXISTS bankAccount (id INT PRIMARY KEY," +
        " balance VARCHAR(30), currencyCode VARCHAR(3), currencySymbol VARCHAR(3)," +
        " state VARCHAR(10), accountType VARCHAR(10), interest VARCHAR(30), cfOwner VARCHAR(16))"
      connection.createStatement.execute(query)
      true
    else false

  override def initialize(): Unit =
    if tableCreatedNow then
      populateDB()

  override def insert(entity: BankAccount): Unit =
    val query = "INSERT INTO bankAccount (id, balance, currencyCode, currencySymbol, state, accountType, cfOwner) VALUES (?, ?, ?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, entity.id)
    stmt.setString(2, entity.balance.toString)
    stmt.setString(3, entity.currency.code)
    stmt.setString(4, entity.currency.symbol)
    stmt.setString(5, entity.state.toString)
    stmt.setString(6, entity.bankAccountType.nameType)
    stmt.setString(7, entity.customer.cf)
    stmt.executeUpdate

  private def createBankAccount(resultSet: ResultSet): BankAccount =
    val id = resultSet.getInt("id")
    fetchedBankAccounts.get(id) match
      case Some(b) => b
      case None =>
        val balance = resultSet.getString("balance")
        val currency = Currency(resultSet.getString("currencyCode"), resultSet.getString("currencySymbol"))
        val state = StateBankAccount.valueOf(resultSet.getString("state"))
        val accountTypeName = resultSet.getString("accountType")
        val accountType = bankAccountTypeTable.findById(accountTypeName).get
        val customer = customerTable.findById(resultSet.getString("cfOwner")).get
        val acc = BankAccount(id, customer, balance.toMoney, currency, state, accountType)
        fetchedBankAccounts.put(acc.id, acc)
        movementTable.findByBankAccount(id)
          .foreach(acc.addMovement)
        acc

  override def findById(id: Int): Option[BankAccount] =
    val query = "SELECT * FROM bankAccount WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    val result = stmt.executeQuery
    if (result.next) Some(createBankAccount(result)) else None

  private def toIterator(resultSet: ResultSet): Seq[BankAccount] =
    new Iterator[BankAccount]:
      def hasNext: Boolean = resultSet.next
      def next(): BankAccount = createBankAccount(resultSet)
    .toSeq

  def findByCustomerCf(customerCf: String): Seq[BankAccount] =
    val stmt = connection.prepareStatement("SELECT * FROM bankAccount WHERE cfOwner = ?")
    stmt.setString(1, customerCf)
    val resultSet = stmt.executeQuery
    toIterator(resultSet)

  override def findAll(): Seq[BankAccount] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM bankAccount"
    val resultSet = stmt.executeQuery(query)
    toIterator(resultSet)

  override def update(entity: BankAccount): Unit =
    val query = "UPDATE bankAccount SET balance = ?, currencyCode = ?, " +
      "currencySymbol = ?, state = ?, accountType = ? WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.balance.toString)
    stmt.setString(2, entity.currency.code)
    stmt.setString(3, entity.currency.symbol)
    stmt.setString(4, entity.state.toString)
    stmt.setString(5, entity.bankAccountType.nameType)
    stmt.setInt(6, entity.id)
    stmt.executeUpdate
    fetchedBankAccounts.remove(entity.id)

  override def delete(id: Int): Unit =
    val query = "DELETE FROM bankAccount WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    stmt.executeUpdate
    fetchedBankAccounts.remove(id)
  
  private def populateDB(): Unit =
    val customers = customerTable.findAll()
    val bankAccountTypes = bankAccountTypeTable.findAll()
    val currency = currencyTable.findById("USD").getOrElse(currencyTable.findAll().head)
    var idCounter = 1
    val bankAccounts = for
      customer <- customers
      accountType <- bankAccountTypes
    yield
      val id = idCounter
      idCounter += 1
      println(idCounter)
      val balance = Random.nextInt(10000).toMoney
      val state = StateBankAccount.Active
      BankAccount(id, customer, balance, currency, state, accountType)
    bankAccounts.foreach(insert)

