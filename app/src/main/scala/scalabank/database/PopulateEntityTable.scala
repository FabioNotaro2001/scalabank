package scalabank.database

import scalabank.database.FiscalCode
import scalabank.entities.Person

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * Populates a table with randomly generated entity instances.
 */
object PopulateEntityTable:

  /**
   * Creates a list of randomly generated instances and stores them in a database.
   *
   * @param numberOfEntries The number of entries to create.
   * @param createEntity    A function that creates an entity given a fiscal code, name, surname, and birth year.
   * @tparam T The type of the entity to create.
   * @return A list of created entities.
   */
  def createInstancesDB[T](numberOfEntries: Int, createEntity: (String, String, String, Int) => T): List[T] =
    val random = new Random()
    val names = List("Mario", "Luigi", "Giovanni", "Francesca", "Luca", "Alessandro", "Chiara", "Giulia", "Martina", "Simone")
    val surnames = List("Rossi", "Bianchi", "Verdi", "Neri", "Gialli", "Ferri", "Russo", "Gallo", "Santoro", "Marino")

    val peopleList = ListBuffer.empty[T]
    for _ <- 1 to numberOfEntries do
      val name = names(random.nextInt(names.length))
      val surname = surnames(random.nextInt(surnames.length))
      val birthYear = 1950 + random.nextInt(71) // Random birth year between 1950 and 2020
      val cf = FiscalCode.generateFiscalCode(name, surname, birthYear)
      val person = createEntity(cf, name, surname, birthYear)
      peopleList += person
    peopleList.toList
