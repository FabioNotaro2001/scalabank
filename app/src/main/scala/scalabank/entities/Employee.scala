package scalabank.entities

trait Employee extends Staff

object Employee:
  def apply(name: String, surname: String, birthYear: Int): Employee = EmployeeImpl(name, surname, birthYear)

  private case class EmployeeImpl(_name: String, _surname: String, _birthYear: Int) extends Employee:
    private val person = Person(_name, _surname, _birthYear)

    def position: String = ???

    def salary: Double = ???

    export person.*
