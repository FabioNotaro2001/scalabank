package scalabank.testLogger

import org.scalatest.matchers.should.Matchers.*
import scalabank.logger.*
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class TestLogger extends AnyFlatSpec:
  "A logger" should "print" in {
    Logger.log("ciao")
  }


