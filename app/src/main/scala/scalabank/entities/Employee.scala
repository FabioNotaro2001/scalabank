package scalabank.entities

trait Employee extends StaffMember

object Employee:
  enum Position:
    case Cashier
    case FinancialAnalyst

  def apply(name: String, surname: String, birthYear: Int, position: Position): Employee = EmployeeImpl(name, surname, birthYear, position)

  private case class EmployeeImpl(override val name: String, override val surname: String, override val birthYear: Int, override val position: Position) extends Employee:
    private val person = Person(name, surname, birthYear)
    export person.{isAdult, age}

    def salary: Double = ???


