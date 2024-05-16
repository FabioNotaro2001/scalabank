package scalabank.entities

import scalabank.entities.Employee.PositionEmployee

trait Employee extends StaffMember[PositionEmployee]

object Employee:
  enum PositionEmployee(val salary: Double):
    case Cashier extends PositionEmployee(1000)
    case FinancialAnalyst extends PositionEmployee(1500)

  given taxRateDefault: Double = 0.2

  def apply(name: String, surname: String, birthYear: Int, position: PositionEmployee): Employee = EmployeeImpl(Person(name, surname, birthYear), position)

  private case class EmployeeImpl(person: Person, override val position: PositionEmployee) extends Employee:
    export person.*
    def salary: Double = position.salary

  extension (employee: Employee)
    def fullName: String = s"${employee.name} ${employee.surname}"
    def annualSalaryWithBonus(bonus: Double): Double = employee.annualSalary + bonus

