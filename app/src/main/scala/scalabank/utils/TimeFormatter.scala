package scalabank.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Trait defining the functionality for formatting the current time.
 */
trait TimeFormatter:
  /**
   * Gets the current time formatted as a string.
   *
   * @return a string representing the current time formatted as "HH:mm:ss".
   */
  def getTimeFormatted: String

/**
 * Companion object for the TimeFormatter trait.
 */
object TimeFormatter:
  /**
   * Creates a new instance of TimeFormatter.
   *
   * @return a new TimeFormatter instance.
   */
  def apply(): TimeFormatter = TimeFormatterImpl()

  /**
   * Private class implementing the TimeFormatter trait.
   */
  private class TimeFormatterImpl extends TimeFormatter:
    /**
     * Gets the current time formatted as a string.
     *
     * @return a string representing the current time formatted as "HH:mm:ss".
     */
    override def getTimeFormatted: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))