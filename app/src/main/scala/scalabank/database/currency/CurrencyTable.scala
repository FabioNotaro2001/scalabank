package scalabank.database.currency

import scalabank.database.{Database, DatabaseOperations}
import scalabank.currency.Currency
import java.sql.{Connection, ResultSet}

/**
 * Class representing the currency table in the database.
 *
 * @param connection The database connection to use.
 * @param database The database reference.
 */
class CurrencyTable(override val connection: Connection, override val database: Database) extends DatabaseOperations[Currency, String]:

  private val tableCreated =
    if !tableExists("currency", connection) then
      val query = "CREATE TABLE IF NOT EXISTS currency (code VARCHAR(10) PRIMARY KEY, symbol VARCHAR(10))"
      connection.createStatement().execute(query)
      true
    else false

  override def initialize(): Unit =
    if tableCreated then
      populateDB()

  override def insert(currency: Currency): Unit =
    val query = "INSERT INTO currency (code, symbol) VALUES (?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, currency.code)
    stmt.setString(2, currency.symbol)
    stmt.executeUpdate

  private def createCurrency(resultSet: ResultSet): Currency =
    Currency(resultSet.getString("code"), resultSet.getString("symbol"))

  override def findById(code: String): Option[Currency] =
    val stmt = connection.prepareStatement("SELECT * FROM currency WHERE code = ?")
    stmt.setString(1, code)
    val result = stmt.executeQuery
    if result.next then Some(createCurrency(result)) else None

  override def findAll(): Seq[Currency] =
    val stmt = connection.createStatement
    val resultSet = stmt.executeQuery("SELECT * FROM currency")
    new Iterator[Currency]:
      def hasNext: Boolean = resultSet.next
      def next(): Currency = createCurrency(resultSet)
    .toSeq

  override def update(currency: Currency): Unit =
    val query = "UPDATE currency SET symbol = ? WHERE code = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, currency.symbol)
    stmt.setString(2, currency.code)
    stmt.executeUpdate

  override def delete(code: String): Unit =
    val query = "DELETE FROM currency WHERE code = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, code)
    stmt.executeUpdate

  private def populateDB(): Unit =
    List(
      Currency("USD", "$"),
      Currency("EUR", "€"),
      Currency("JPY", "¥")
    ).foreach(insert)

