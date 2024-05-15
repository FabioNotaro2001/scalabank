package scalabank.entities

trait Employee extends StaffMember

object Employee:
  enum Position(val salary: Double):
    case Cashier extends Position(1000)
    case FinancialAnalyst extends Position(1500)


  def apply(name: String, surname: String, birthYear: Int, position: Position): Employee = EmployeeImpl(Person(name, surname, birthYear), position)

  private case class EmployeeImpl(person: Person, override val position: Position) extends Employee:
    export person.*

    def salary: Double = position.salary


