package scalabank.entities

import scalabank.entities.Employee.EmployeePosition
import scalabank.logger.{Logger, LoggerDependency, LoggerImpl}

/**
 * Trait representing the Employee, extending StaffMember with a specific EmployeePosition and Promotable behavior.
 */
trait Employee extends StaffMember[EmployeePosition] with Promotable[EmployeePosition]

/**
 * Represents the promotion a staff member to a new position.
 *
 * @tparam T the type of the staff position
 */
trait Promotable[T <: StaffPosition]:
  def promote(newPosition: T): Employee

trait EmployeeComponent:
  loggerDependency: LoggerDependency =>
  case class EmployeeImpl(person: Person, override val position: EmployeePosition, override val hiringYear: Int) extends Employee:
    export person.*
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    override def promote(newPosition: EmployeePosition): Employee = copy(position = newPosition)

/**
 * Companion object for Employee.
 */
object Employee extends LoggerDependency with EmployeeComponent:
  override val logger: Logger = LoggerImpl()

  /**
   * Enumeration representing the different positions of employees and their salaries.
   */
  enum EmployeePosition(val salary: Double) extends StaffPosition:
    case Cashier extends EmployeePosition(1000)
    case FinancialAnalyst extends EmployeePosition(1500)

  /**
   * Given for default tax rate.
   */
  given taxRateDefault: Double = 0.2

  /**
   * Given instance for the standard bonus rate.
   */
  given standardBonusRate: BonusRate with
    def bonusRate: Double = 0.1

  /**
   * Given instance for the senior bonus rate.
   */
  given seniorBonusRate: BonusRate with
    def bonusRate: Double = 0.2

  /**
   * Factory method to create a new Employee instance.
   *
   * @param name       the employee name.
   * @param surname    the employee surname.
   * @param birthYear  the employee birth year.
   * @param position   the employee position.
   * @param hiringYear the employee hiring year.
   * @return a new Employee instance.
   */
  def apply(cf: String, name: String, surname: String, birthYear: Int, position: EmployeePosition, hiringYear: Int): Employee =
    val employee = EmployeeImpl(Person(cf, name, surname, birthYear), position, hiringYear)
    employee

  extension (employee: Employee)
    /**
     * Gets employee full name.
     *
     * @return employee full name.
     */
    def fullName: String = s"${employee.name} ${employee.surname}"

    /**
     * Calculates the annual salary with bonus.
     *
     * @param bonusRate  bonus to apply.
     * @return the annual salary with the bonus.
     */
    def annualSalaryWithContextualBonus(using bonusRate: BonusRate): Double =
      employee.annualSalary * (1 + bonusRate.bonusRate)

  extension (employees: List[Employee])

    /**
     * Gets annual salaries of all employees.
     *
     * @return a list of annual salaries.
     */
    def allEmployeesSalary: List[Double] =
      for
        e <- employees
      yield e.annualSalary

    /**
     * Calculates total annual salary of all employees.
     *
     * @return the total annual salary.
     */
    def totalAnnualSalary: Double =
      (for e <- employees yield e.annualSalary).sum