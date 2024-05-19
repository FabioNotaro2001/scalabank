package scalabank.logger

trait Logger:
  def log(string: String): Unit

object Logger:
  def apply(on: Boolean): Logger = LoggerImpl(on)

  private case class LoggerImpl(on: Boolean) extends Logger:

    override def log(string: String): Unit = println(string)