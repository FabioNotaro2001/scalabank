package scalabank.entities

import scalabank.entities.Employee.PositionEmployee

trait Employee extends StaffMember[PositionEmployee]

object Employee:
  enum PositionEmployee(val salary: Double):
    case Cashier extends PositionEmployee(1000)
    case FinancialAnalyst extends PositionEmployee(1500)

  def apply(name: String, surname: String, birthYear: Int, position: PositionEmployee): Employee = EmployeeImpl(Person(name, surname, birthYear), position)

  private case class EmployeeImpl(person: Person, override val position: PositionEmployee) extends Employee:
    export person.*
    def salary: Double = position.salary



