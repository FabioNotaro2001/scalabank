package scalabank.logger

import scalabank.utils.TimeFormatter

/**
 * Trait defining the functionality for formatting log message prefixes.
 */
trait PrefixFormatter:
  /**
   * Gets the prefix with the current time.
   *
   * @return a string representing the prefix with the current time.
   */
  def getPrefixWithCurrentTime: String

  /**
   * Gets the creation prefix.
   *
   * @return a string representing the creation prefix.
   */
  def getCreationPrefix: String

/**
 * Companion object for the PrefixFormatter trait.
 */
object PrefixFormatter:
  /**
   * Creates a new instance of PrefixFormatter.
   *
   * @return a new PrefixFormatter instance.
   */
  def apply(): PrefixFormatter = PrefixFormatterImpl()

  /**
   * Private class implementing the PrefixFormatter trait.
   */
  private class PrefixFormatterImpl extends PrefixFormatter:
    private val timeFormatter = TimeFormatter()

    /**
     * Gets the prefix with the current time.
     *
     * @return a string representing the prefix with the current time.
     */
    override def getPrefixWithCurrentTime: String = "[" + timeFormatter.getTimeFormatted() + "] "

    /**
     * Gets the creation prefix.
     *
     * @return a string representing the creation prefix.
     */
    override def getCreationPrefix: String = "[CREATION] "