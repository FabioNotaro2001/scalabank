package scalabank.entities

/**
 * Represents a person with basic information: name, surname, birth year, and age.
 */
trait Person:
  /**
   * The person fiscal code.
   *
   * @return The person fiscal code.
   */
  def cf: String
  /**
   * The person name.
   *
   * @return The name of the person.
   */
  def name: String

  /**
   * The person surname.
   *
   * @return The surname of the person.
   */
  def surname: String

  /**
   * The person birth.
   *
   * @return The birth year of the person.
   */
  def birthYear: Int

  /**
   * The person age.
   *
   * @return The age of the person.
   */
  def age: Int

  /**
   * The person is an adult (age >= 18), false otherwise.
   *
   * @return True if the person is an adult, false otherwise.
   */
  def isAdult: Boolean

/**
 * Factory for creating Person instances.
 */
object Person:

  /**
   * Creates a new Person instance.
   *
   * @param name      The person name.
   * @param surname   The person surname.
   * @param birthYear The person birth year.
   * @return A new Person instance.
   */
  def apply(cf: String, name: String, surname: String, birthYear: Int): Person = PersonImpl(cf, name, surname, birthYear)

  private case class PersonImpl(override val cf: String,
                                override val name: String,
                                override val surname: String,
                                override val birthYear: Int) extends Person:

    import java.time.LocalDate
    private val currentYear: Int = LocalDate.now.getYear

    require(birthYear <= currentYear, s"Birth year $birthYear cannot be in the future")

    override def age: Int = currentYear - birthYear

    override def isAdult: Boolean = age >= 18

    override def toString: String = s"$name $surname, Age: $age, Adult: $isAdult"

  extension (person: Person)

    /**
     * Checks if the current person is younger than the specified other person.
     *
     * @param other The other person to compare against.
     * @return True if the current person is younger than the other person, false otherwise.
     */
    def isYoungerThan(other: Person): Boolean = person.birthYear > other.birthYear

    /**
     * Calculates the age difference between the current person and the specified other person.
     *
     * @param other The other person to calculate the age difference with.
     * @return The absolute value of the difference in birth years between the two persons.
     */
    def ageDifference(other: Person): Int = Math.abs(person.birthYear - other.birthYear)
