package scalabank.database

trait DatabaseOperations[T]:
  def insert(entity: T): Unit
  def findById(id: Long): Option[T]
  def findAll(): Seq[T]
  def update(entity: T): Unit
  def delete(id: Long): Unit
