package scalabank.database.bank

import scalabank.currency.Currency
import scalabank.database.DatabaseOperations
import scalabank.entities.*
import scalabank.currency.MoneyADT.*

import java.sql.{Connection, ResultSet}

class BankAccountTable(val connection: Connection) extends DatabaseOperations[BankAccount, Int] :
  if !tableExists("bankAccount", connection) then
    val query = "CREATE TABLE IF NOT EXISTS bankAccount (id INT PRIMARY KEY," +
      " balance VARCHAR(30), currencyCode VARCHAR(3), currencySymbol VARCHAR(3)," +
      " state VARCHAR(10), accountType VARCHAR(10))"
    connection.createStatement.execute(query)
    populateDB()

  private def bankAccountType(entity: BankAccount) = entity match
    case _: BaseBankAccount.BaseBankAccountImpl => "Base"
    case _: SuperBankAccount.SuperBankAccountImpl => "Super"

  def insert(entity: BankAccount): Unit =
    val query = "INSERT INTO bankAccount (id, balance, currencyCode, currencySymbol, state, accountType) VALUES (?, ?, ?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, entity.id)
    stmt.setString(2, entity.balance.toString)
    stmt.setString(3, entity.currency.code)
    stmt.setString(4, entity.currency.symbol)
    stmt.setString(5, entity.state.toString)
    stmt.setString(6, bankAccountType(entity))
    stmt.executeUpdate

  private def createBankAccount(resultSet: ResultSet): BankAccount =
    val id = resultSet.getInt("id")
    val balance = resultSet.getString("balance")
    val currency = Currency(resultSet.getString("currencyCode"), resultSet.getString("currencySymbol"))
    val state = StateBankAccount.valueOf(resultSet.getString("state"))
    val accountType = resultSet.getString("accountType")

    accountType match
      case "Base"  => BaseBankAccount(id, balance.toMoney, currency, state)
      case "Super" => SuperBankAccount(id, balance.toMoney, currency, state)


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
      "currencySymbol = ?, state = ?, accountType = ? WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.balance.toString)
    stmt.setString(2, entity.currency.code)
    stmt.setString(3, entity.currency.symbol)
    stmt.setString(4, entity.state.toString)
    stmt.setString(5, bankAccountType(entity))
    stmt.setInt(6, entity.id)
    stmt.executeUpdate

  def delete(id: Int): Unit =
    val query = "DELETE FROM bankAccount WHERE id = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setInt(1, id)
    stmt.executeUpdate

  def populateDB(): Unit =
    List(
      BaseBankAccount(1, 1000.toMoney, Currency("USD", "$"), StateBankAccount.Active),
      SuperBankAccount(2, 2000.00.toMoney, Currency("EUR", "€"), StateBankAccount.Active)
    ).foreach(insert)
