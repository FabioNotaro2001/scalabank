package scalabank.logger

trait OutputMedia:
  def write(string: String): Unit

object Console:
  class Console extends OutputMedia:
    override def write(string: String): Unit = println(string)