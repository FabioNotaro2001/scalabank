package scalabank.logger

import scalabank.utils.TimeFormatter

trait PrefixFormatter:
  def getStandardPrefixWithCurrentTime: String
  def getCreationPrefix: String

object PrefixFormatter extends PrefixFormatter:
    private val timeFormatter = TimeFormatter()
    override def getStandardPrefixWithCurrentTime: String = "[" + timeFormatter.getTimeFormatted() + "] "
    override def getCreationPrefix: String = "[CREATION] "