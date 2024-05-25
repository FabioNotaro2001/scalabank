package scalabank.logger

import scala.collection.mutable.ListBuffer

object Logger:
    type Event = String

    private var savedEventsList = ListBuffer[Event]()
    private val prefix: String = PrefixFormatter().getStandardPrefixFormatter

    def log(event: Event): Unit = println(event)
    def save(event: Event): Unit = savedEventsList += (prefix + event)
    def getSize(): Int = savedEventsList.size
    def reset(): Unit = savedEventsList.clear()
    def logAll(): Unit =
        savedEventsList.foreach(e => Logger.log(e))
        Logger.reset()