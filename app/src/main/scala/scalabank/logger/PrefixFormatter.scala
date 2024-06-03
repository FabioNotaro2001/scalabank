package scalabank.logger

import scalabank.utils.TimeFormatter

trait PrefixFormatter:
  def getPrefixWithCurrentTime: String
  def getCreationPrefix: String

object PrefixFormatter:
  def apply(): PrefixFormatter = PrefixFormatterImpl()
  private class PrefixFormatterImpl extends PrefixFormatter:
    private val timeFormatter = TimeFormatter()
    override def getPrefixWithCurrentTime: String = "[" + timeFormatter.getTimeFormatted() + "] "
    override def getCreationPrefix: String = "[CREATION] "