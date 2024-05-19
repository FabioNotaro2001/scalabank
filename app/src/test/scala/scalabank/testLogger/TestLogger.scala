package scalabank.testLogger

import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.Logger
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.*


@RunWith(classOf[JUnitRunner])
class TestLogger extends AnyFlatSpec:
  val logger = Logger(true)

  "A logger" should "print" in {
    logger log "ciao"
  }


