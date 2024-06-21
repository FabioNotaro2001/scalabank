package scalabank.database

import java.sql.{Connection, ResultSet}

trait DatabaseOperations[T, Q]:
  def insert(entity: T): Unit
  def findById(id: Q): Option[T]
  def findAll(): Seq[T]
  def update(entity: T): Unit
  def delete(id: Q): Unit
  def tableExists(tableName: String, connection: Connection): Boolean =
    val statement = connection.createStatement
    try
      val query = s"SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '$tableName'"
      val resultSet: ResultSet = statement.executeQuery(query)
      resultSet.next
    finally
      statement.close()


