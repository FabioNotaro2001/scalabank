package scalabank.database

import scala.util.Random

/**
 * Object for generating fiscal codes based on personal information.
 */
object FiscalCode:
  private def extractConsonants(str: String): String =
    str.filter(_.isLetter).filterNot("AEIOUaeiou".contains(_)).toUpperCase

  private def extractVowels(str: String): String =
    str.filter(_.isLetter).filter("AEIOUaeiou".contains(_)).toUpperCase

  private def padToThreeCharacters(str: String): String =
    str.padTo(3, 'X').take(3)

  /**
   * Generates a fiscal code based on name, surname, and birth year.
   *
   * @param name      The person's first name.
   * @param surname   The person's surname.
   * @param birthYear The person's year of birth.
   * @return A string representing the generated fiscal code.
   */
  def generateFiscalCode(name: String, surname: String, birthYear: Int): String =
    val nameConsonants = extractConsonants(name)
    val nameVowels = extractVowels(name)
    val surnameConsonants = extractConsonants(surname)
    val surnameVowels = extractVowels(surname)
    val codeSurname = padToThreeCharacters(surnameConsonants + surnameVowels)
    val codeName = padToThreeCharacters(nameConsonants + nameVowels)
    val year = birthYear.toString.takeRight(2)
    val months = "ABCDEHLMPRST"
    val month = months(Random.nextInt(months.length))
    val day = "%02d".format(1 + Random.nextInt(28))
    val controlChar = Random.alphanumeric.filter(_.isLetter).head.toUpper
    codeSurname + codeName + year + month + day + controlChar
