package scalabank.database.person

import scalabank.database.DatabaseOperations
import scalabank.entities.Person
import scalabank.database.PopulateEntityTable

import java.sql.{Connection, ResultSet}

/**
 * Class representing the person table in the database.
 *
 * @param connection The database connection to use.
 */
class PersonTable(val connection: Connection) extends DatabaseOperations[Person, String]:
  if !tableExists("person", connection) then
    val query = "CREATE TABLE IF NOT EXISTS person (cf VARCHAR(16) PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), birthYear INT)"
    connection.createStatement().execute(query)
    populateDB(1)

  def insert(entity: Person): Unit =
    val query = "INSERT INTO person (cf, name, surname, birthYear) VALUES (?, ?, ?, ?)"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.cf)
    stmt.setString(2, entity.name)
    stmt.setString(3, entity.surname)
    stmt.setInt(4, entity.birthYear)
    stmt.executeUpdate

  private def createPerson(resultSet: ResultSet) =
    Person(resultSet.getString("cf"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getInt("birthYear"))

  def findById(cf: String): Option[Person] =
    val stmt = connection.prepareStatement("SELECT * FROM person WHERE cf = ?")
    stmt.setString(1, cf)
    val result = stmt.executeQuery
    for
      _ <- Option(result) if result.next
    yield createPerson(result)

  def findAll(): Seq[Person] =
    val stmt = connection.createStatement
    val resultSet = stmt.executeQuery("SELECT * FROM person")
    new Iterator[Person]:
      def hasNext: Boolean = resultSet.next
      def next(): Person = createPerson(resultSet)
    .toSeq

  def update(entity: Person): Unit =
    val query = "UPDATE person SET name = ?, surname = ?, birthYear = ? WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, entity.name)
    stmt.setString(2, entity.surname)
    stmt.setInt(3, entity.birthYear)
    stmt.setString(4, entity.cf)
    stmt.executeUpdate

  def delete(cf: String): Unit =
    val query = "DELETE FROM person WHERE cf = ?"
    val stmt = connection.prepareStatement(query)
    stmt.setString(1, cf)
    stmt.executeUpdate

  private def populateDB(numberOfEntries: Int): Unit =
      PopulateEntityTable.createInstancesDB(numberOfEntries, (cf, name, surname, birthYear) => Person(cf, name, surname, birthYear)).foreach(_ => insert(_))



