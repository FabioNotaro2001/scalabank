package scalabank.database

import java.sql.{Connection, ResultSet}

/**
 * Trait defining standard database operations for a generic entity type.
 *
 * @tparam T The type of the entity.
 * @tparam Q The type of the entity's identifier.
 */
trait DatabaseOperations[T, Q]:
  /**
   * @return the database containing the table
   */
  def database: Database

  /**
   * Initializes the object
   */
  def initialize(): Unit

  /**
   * Inserts a new entity into the database.
   *
   * @param entity The entity to insert.
   */
  def insert(entity: T): Unit

  /**
   * Finds an entity by its identifier.
   *
   * @param id The identifier of the entity.
   * @return An option containing the found entity or None if not found.
   */
  def findById(id: Q): Option[T]

  /**
   * Retrieves all entities from the database.
   *
   * @return A sequence of all entities.
   */
  def findAll(): Seq[T]

  /**
   * Updates an existing entity in the database.
   *
   * @param entity The entity to update.
   */
  def update(entity: T): Unit

  /**
   * Deletes an entity by its identifier.
   *
   * @param id The identifier of the entity to delete.
   */
  def delete(id: Q): Unit

  /**
   * Checks if a table exists in the database.
   *
   * @param tableName  The name of the table.
   * @param connection The database connection to use.
   * @return True if the table exists, false otherwise.
   */
  def tableExists(tableName: String, connection: Connection): Boolean =
    val metaData = connection.getMetaData
    val resultSet = metaData.getTables(null, null, tableName.toUpperCase, null)
    try
      resultSet.next()
    finally
      resultSet.close()
    /*val statement = connection.createStatement
    try
      val query = s"SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '$tableName'"
      val resultSet: ResultSet = statement.executeQuery(query)  // FIXME: errato
      resultSet.next
    finally
      statement.close()*/






