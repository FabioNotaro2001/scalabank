package scalabank.logger

import scala.collection.mutable.ListBuffer

object Logger:
    private val savedStrings = ListBuffer[String]()
    def log(string: String): Unit = println(string)
    def save(string: String): Unit = savedStrings += string
    def getSize(): Int = savedStrings.size
    def reset(): Unit = savedStrings.clear()