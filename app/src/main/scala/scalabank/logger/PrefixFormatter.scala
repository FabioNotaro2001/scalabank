package scalabank.logger

import scalabank.utils.TimeFormatter

trait PrefixFormatter:
  def getStandardPrefixFormatter: String

object PrefixFormatter:
  def apply(): PrefixFormatter = PrefixFormatterImpl()
  private class PrefixFormatterImpl extends PrefixFormatter:
    val timeFormatter = TimeFormatter()
    override def getStandardPrefixFormatter: String = "[" + timeFormatter.getTimeFormatted() + "] "

