package scalabank.database.employee

import scalabank.entities.{Employee, Person}
import scalabank.entities.Employee.EmployeePosition
import scalabank.database.FiscalCode
import scala.collection.mutable.ListBuffer
import scala.util.Random

object PopulateEmployeeTable:

  def createInstancesDB(numberOfEntries: Int): List[Employee] =
    val random = new Random()
    val names = List("Mario", "Luigi", "Giovanni", "Francesca", "Luca", "Alessandro", "Chiara", "Giulia", "Martina", "Simone")
    val surnames = List("Rossi", "Bianchi", "Verdi", "Neri", "Gialli", "Ferri", "Russo", "Gallo", "Santoro", "Marino")
    val positions = EmployeePosition.values

    val employeeList = ListBuffer.empty[Employee]
    for _ <- 1 to numberOfEntries do
      val name = names(random.nextInt(names.length))
      val surname = surnames(random.nextInt(surnames.length))
      val birthYear = 1950 + random.nextInt(71) // Random birth year between 1950 and 2020
      val position = positions(random.nextInt(positions.length))
      val hiringYear = 2000 + random.nextInt(21) // Random hiring year between 2000 and 2020
      val cf = FiscalCode.generateFiscalCode(name, surname, birthYear)
      val employee = Employee(cf, name, surname, birthYear, position, hiringYear)
      employeeList += employee
    employeeList.toList