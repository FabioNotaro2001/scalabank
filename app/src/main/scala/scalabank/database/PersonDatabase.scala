package scalabank.database

import scalabank.database.DatabaseSetup.conn
import scalabank.entities.Person
import java.sql.ResultSet

class PersonDatabase extends DatabaseOperations[Person]:
  private val query = "CREATE TABLE IF NOT EXISTS person (cf VARCHAR(12) PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), birthYear INT)"
  conn.createStatement().execute(query)

  def insert(entity: Person): Unit =
    val stmt = conn.prepareStatement("INSERT INTO person (cf, name, surname, birthYear) VALUES (?, ?, ?, ?)")
    stmt.setString(1, entity.cf)
    stmt.setString(2, entity.name)
    stmt.setInt(3, entity.age)
    stmt.executeUpdate()

  private def createPerson(resultSet: ResultSet) =
    Person(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getInt("birthYear"))

  def findById(cf: String): Option[Person] =
    val stmt = conn.prepareStatement("SELECT * FROM person WHERE cf = ?")
    stmt.setString(1, cf)
    val rs = stmt.executeQuery()
    if (rs.next())
      Some(createPerson(rs))
    else
      None

  def findAll(): Seq[Person] =
    val stmt = conn.createStatement()
    val resultSet = stmt.executeQuery("SELECT * FROM person")
    new Iterator[Person]:
      def hasNext: Boolean = resultSet.next()
      def next(): Person = createPerson(resultSet)
    .toSeq

  def update(entity: Person): Unit =
    val stmt = conn.prepareStatement("UPDATE person SET name = ?, surname = ?, birthYear = ? WHERE cf = ?")
    stmt.setString(1, entity.name)
    stmt.setString(2, entity.surname)
    stmt.setInt(3, entity.birthYear)
    stmt.setString(4, entity.cf)
    stmt.executeUpdate()

  def delete(cf: String): Unit =
    val stmt = conn.prepareStatement("DELETE FROM person WHERE cf = ?")
    stmt.setString(1, cf)
    stmt.executeUpdate()


