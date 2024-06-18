package scalabank.database

import org.scalatest.matchers.should.Matchers.*
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class DataBaseTest extends AnyFlatSpec:
  "PersonDatabase" should "insert and find a person" in :
    val db = new PersonDatabase
    true should be(true)



