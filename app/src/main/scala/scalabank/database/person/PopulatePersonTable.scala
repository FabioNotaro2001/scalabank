package scalabank.database.person

import scalabank.database.FiscalCode
import scalabank.entities.Person

import scala.collection.mutable.ListBuffer
import scala.util.Random

object PopulatePersonTable:

  def createInstancesDB(numberOfEntries: Int): List[Person] =
    val random = new Random()
    val names = List("Mario", "Luigi", "Giovanni", "Francesca", "Luca", "Alessandro", "Chiara", "Giulia", "Martina", "Simone")
    val surnames = List("Rossi", "Bianchi", "Verdi", "Neri", "Gialli", "Ferri", "Russo", "Gallo", "Santoro", "Marino")

    val peopleList = ListBuffer.empty[Person]
    for _ <- 1 to numberOfEntries do
      val name = names(random.nextInt(names.length))
      val surname = surnames(random.nextInt(surnames.length))
      val birthYear = 1950 + random.nextInt(71) // Random birth year between 1950 and 2020
      val cf = FiscalCode.generateFiscalCode(name, surname, birthYear)
      val person = Person(cf, name, surname, birthYear)
      peopleList += person
    peopleList.toList
