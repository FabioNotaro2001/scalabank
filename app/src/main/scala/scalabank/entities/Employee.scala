package scalabank.entities

import scalabank.entities.Employee.EmployeePosition

trait Employee extends StaffMember[EmployeePosition] with Promotable[EmployeePosition]

object Employee:
  enum EmployeePosition(val salary: Double) extends StaffPosition:
    case Cashier extends EmployeePosition(1000)
    case FinancialAnalyst extends EmployeePosition(1500)

  given taxRateDefault: Double = 0.2
  given standardBonusRate: BonusRate with
    def bonusRate: Double = 0.1
  given seniorBonusRate: BonusRate with
    def bonusRate: Double = 0.2

  def apply(name: String, surname: String, birthYear: Int, position: EmployeePosition, hiringYear: Int):
    Employee = EmployeeImpl(Person(name, surname, birthYear), position, hiringYear)

  private case class EmployeeImpl(person: Person, override val position: EmployeePosition, override val hiringYear: Int) extends Employee:
    export person.*
    def promote(newPosition: EmployeePosition): Employee = copy(position = newPosition)

  extension (employee: Employee)
    def fullName: String = s"${employee.name} ${employee.surname}"
    def annualSalaryWithContextualBonus(using bonusRate: BonusRate): Double =
      employee.annualSalary * (1 + bonusRate.bonusRate)

  extension (employees: List[Employee])
    def allEmployeesSalary: List[Double] =
      for
        e <- employees
      yield e.annualSalary

    def totalAnnualSalary: Double =
      (for e <- employees yield e.annualSalary).sum