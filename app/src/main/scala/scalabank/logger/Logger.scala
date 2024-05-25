package scalabank.logger

import scalabank.logger.Console.Console

import scala.collection.mutable.ListBuffer

object Logger:
    private type Event = String // TODO: è giusto private qui?????

    private val savedEventsList = ListBuffer[Event]()
    private val prefix: String = PrefixFormatter().getStandardPrefixFormatter
    private var isEnabled = true
    private val outputMedia: OutputMedia = Console()

    def disable(): Unit = isEnabled = false

    def enable(): Unit = isEnabled = true

    def log(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        outputMedia.write(event)

    def save(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        savedEventsList += (prefix + event)

    def logAll(): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        savedEventsList.foreach(e => Logger.log(e))
        Logger.reset()

    def getSize: Int = savedEventsList.size

    def reset(): Unit = savedEventsList.clear()