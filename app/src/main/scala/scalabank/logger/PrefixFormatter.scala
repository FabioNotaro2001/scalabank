package scalabank.logger

import scalabank.utils.TimeFormatter

trait PrefixFormatter:
  def getStandardPrefixWithCurrentTime: String

object PrefixFormatter:
  def apply(): PrefixFormatter = PrefixFormatterImpl()
  private class PrefixFormatterImpl extends PrefixFormatter:
    private val timeFormatter = TimeFormatter()
    override def getStandardPrefixWithCurrentTime: String = "[" + timeFormatter.getTimeFormatted() + "] "