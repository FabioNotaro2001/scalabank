package scalabank.database

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalabank.entities.Employee
import scalabank.entities.Employee.EmployeePosition

@RunWith(classOf[JUnitRunner])
class EmployeeTest extends AnyFlatSpec with Matchers:
  val db: Database = Database("jdbc:h2:mem:test3;DB_CLOSE_DELAY=-1")
  val employee: Employee = Employee("EMP12345L67T890M", "Mario", "Rossi", 1990, EmployeePosition.Cashier, 2015)

  "EmployeeDatabase" should "insert and find an employee" in :
    db.employeeTable.insert(employee)
    val retrievedEmployee = db.employeeTable.findById("EMP12345L67T890M")
    retrievedEmployee should be(defined)
    retrievedEmployee.get shouldEqual employee

  it should "update an employee" in :
    val updatedEmployee = Employee("EMP12345L67T890M", "Luigi", "Bianchi", 1990, EmployeePosition.FinancialAnalyst, 2015)
    db.employeeTable.update(updatedEmployee)
    val retrievedEmployee = db.employeeTable.findById("EMP12345L67T890M")
    retrievedEmployee should be(defined)
    retrievedEmployee.get.name shouldEqual "Luigi"
    retrievedEmployee.get.surname shouldEqual "Bianchi"
    retrievedEmployee.get.position shouldEqual EmployeePosition.FinancialAnalyst

  it should "delete an employee" in :
    db.employeeTable.delete("EMP12345L67T890M")
    val retrievedEmployee = db.employeeTable.findById("EMP12345L67T890M")
    retrievedEmployee should be(empty)

  it should "find all employees" in :
    val employee1 = Employee("EMP67890A12B345C", "Mario", "Pastori", 1990, EmployeePosition.Cashier, 2015)
    val employee2 = Employee("EMP09876D34E567F", "Luigi", "Verdi", 1985, EmployeePosition.FinancialAnalyst, 2010)
    db.employeeTable.insert(employee1)
    db.employeeTable.insert(employee2)
    val allEmployees = db.employeeTable.findAll()
    allEmployees should contain allOf(employee1, employee2)
