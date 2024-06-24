package scalabank.database.bank

import scalabank.bank.BankAccountType
import scalabank.currency.Currency
import scalabank.database.DatabaseOperations
import scalabank.entities.*
import scalabank.currency.MoneyADT.*
import scalabank.database.customer.CustomerTable
import scalabank.bankAccount.{BankAccount, StateBankAccount}

import java.sql.{Connection, ResultSet}
import scala.util.Random

/**
 * Class representing the bank account table in the database.
 *
 * @param connection    The database connection to use.
 * @param customerTable The customer table to reference.
 */
class BankAccountTable(val connection: Connection, val customerTable: CustomerTable) extends DatabaseOperations[BankAccount, Int] :
  if !tableExists("bankAccount", connection) then
    val query = "CREATE TABLE IF NOT EXISTS bankAccount (id INT PRIMARY KEY," +
      " balance VARCHAR(30), currencyCode VARCHAR(3), currencySymbol VARCHAR(3)," +
      " state VARCHAR(10), accountType VARCHAR(10), fee VARCHAR(30), cfOwner VARCHAR(16))"
    connection.createStatement.execute(query)
    populateDB()

  def insert(entity: BankAccount): Unit =
    val query = "INSERT INTO bankAccount (id, balance, currencyCode, currencySymbol, state, accountType, fee, cfOwner) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, entity.id)
    stmt.setString(2, entity.balance.toString)
    stmt.setString(3, entity.currency.code)
    stmt.setString(4, entity.currency.symbol)
    stmt.setString(5, entity.state.toString)
    stmt.setString(6, entity.bankAccountType.nameType)
    stmt.setString(7, entity.bankAccountType.feePerOperation.toString())
    stmt.setString(8, entity.customer.cf)
    stmt.executeUpdate

  private def createBankAccount(resultSet: ResultSet): BankAccount =
    val id = resultSet.getInt("id")
    val balance = resultSet.getString("balance")
    val currency = Currency(resultSet.getString("currencyCode"), resultSet.getString("currencySymbol"))
    val state = StateBankAccount.valueOf(resultSet.getString("state"))
    val accountType = BankAccountType(resultSet.getString("accountType"), resultSet.getString("fee").toMoney)
    val customer = customerTable.findById(resultSet.getString("cfOwner")).get
    BankAccount(id, customer, balance.toMoney, currency, state, accountType)


  def findById(id: Int): Option[BankAccount] =
    val query = "SELECT * FROM bankAccount WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    val result = stmt.executeQuery
    if (result.next) Some(createBankAccount(result)) else None


  def findAll(): Seq[BankAccount] =
    val stmt = connection.createStatement
    val query = "SELECT * FROM bankAccount"
    val resultSet = stmt.executeQuery(query)
    new Iterator[BankAccount] :
      def hasNext: Boolean = resultSet.next
      def next(): BankAccount = createBankAccount(resultSet)
    .toSeq

  def update(entity: BankAccount): Unit =
    val query = "UPDATE bankAccount SET balance = ?, currencyCode = ?, " +
      "currencySymbol = ?, state = ?, accountType = ?, fee = ? WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.balance.toString)
    stmt.setString(2, entity.currency.code)
    stmt.setString(3, entity.currency.symbol)
    stmt.setString(4, entity.state.toString)
    stmt.setString(5, entity.bankAccountType.nameType)
    stmt.setString(6, entity.bankAccountType.feePerOperation.toString())
    stmt.setInt(7, entity.id)
    stmt.executeUpdate

  def delete(id: Int): Unit =
    val query = "DELETE FROM bankAccount WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    stmt.executeUpdate

  private def populateDB(): Unit =
    val customers = customerTable.findAll()
    val bankAccountTypes = Seq(
      BankAccountType("Checking", 0.01.toMoney),
      BankAccountType("Savings", 0.02.toMoney),
      BankAccountType("Business", 0.015.toMoney)
    )
    var idCounter = 1
    val bankAccounts = for
      customer <- customers
      accountType <- bankAccountTypes
    yield
      val id = idCounter
      idCounter += 1
      println(idCounter)
      val balance = Random.nextInt(10000).toMoney
      val currency = Currency("USD", "$")
      val state = StateBankAccount.Active
      BankAccount(id, customer, balance, currency, state, accountType)
    bankAccounts.foreach(insert)

