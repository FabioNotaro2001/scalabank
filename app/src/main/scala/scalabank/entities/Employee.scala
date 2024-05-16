package scalabank.entities

import scalabank.entities.Employee.EmployeePosition

trait Employee extends StaffMember[EmployeePosition]

object Employee:
  enum EmployeePosition(val salary: Double) extends StaffPosition:
    case Cashier extends EmployeePosition(1000)
    case FinancialAnalyst extends EmployeePosition(1500)

  given taxRateDefault: Double = 0.2

  def apply(name: String, surname: String, birthYear: Int, position: EmployeePosition): Employee = EmployeeImpl(Person(name, surname, birthYear), position)

  private case class EmployeeImpl(person: Person, override val position: EmployeePosition) extends Employee:
    export person.*

  extension (employee: Employee)
    def fullName: String = s"${employee.name} ${employee.surname}"
    def annualSalaryWithBonus(bonus: Double): Double = employee.annualSalary + bonus

  extension (employees: List[Employee])
    def allEmployeesSalary: List[Double] =
      for
        e <- employees
      yield e.annualSalary
