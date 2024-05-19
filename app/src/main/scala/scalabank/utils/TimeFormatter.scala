package scalabank.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait TimeFormatter:
  def getTimeFormatted(): String

object TimeFormatter:
  def apply(): TimeFormatter = TimeFormatterImpl()

  private class TimeFormatterImpl() extends TimeFormatter:
    override def getTimeFormatted(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))