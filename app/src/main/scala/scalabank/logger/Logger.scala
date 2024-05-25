package scalabank.logger

import scala.collection.mutable.ListBuffer

object Logger:
    type Event = String

    private val savedEventsList = ListBuffer[Event]()
    private val prefix: String = PrefixFormatter().getStandardPrefixFormatter
    private var isEnabled = true

    def log(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        println(event)
    def save(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        savedEventsList += (prefix + event)
    def getSize: Int = savedEventsList.size
    def reset(): Unit = savedEventsList.clear()
    def logAll(): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        savedEventsList.foreach(e => Logger.log(e))
        Logger.reset()
    def disable(): Unit = isEnabled = false
    def enable(): Unit = isEnabled = true