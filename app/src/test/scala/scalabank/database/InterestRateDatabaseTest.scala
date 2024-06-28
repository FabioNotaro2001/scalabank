package scalabank.database

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalabank.loan.InterestRate

@RunWith(classOf[JUnitRunner])
class InterestRateDatabaseTest extends AnyFlatSpec:
  private val database = Database("jdbc:h2:mem:interestRate;DB_CLOSE_DELAY=-1")
  import database.interestTable

  "The InterestTable" should "give the possibility to insert and find an interest rate" in:
    val interestRate = ("testId", InterestRate(0.05))
    interestTable.insert(interestRate)
    val retrievedRate = interestTable.findById("testId")
    retrievedRate should be(defined)
    retrievedRate.get shouldEqual interestRate

  "The InterestTable" should "give the possibility to update an interest rate" in:
    val updatedRate = ("testId", InterestRate(0.06))
    interestTable.update(updatedRate)
    val retrievedRate = interestTable.findById("testId")
    retrievedRate should be(defined)
    retrievedRate.get._2.interestValue shouldEqual 0.06

  "The InterestTable" should "give the possibility to delete an interest rate" in:
    interestTable.delete("testId")
    val retrievedRate = interestTable.findById("testId")
    retrievedRate should be(empty)

  "The InterestTable" should "give the possibility to find all interest rates" in:
    val rate1 = ("rate1", InterestRate(0.04))
    val rate2 = ("rate2", InterestRate(0.03))
    interestTable.insert(rate1)
    interestTable.insert(rate2)
    val allRates = interestTable.findAll()
    allRates should contain allOf(rate1, rate2)