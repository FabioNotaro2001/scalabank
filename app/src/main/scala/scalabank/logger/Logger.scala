package scalabank.logger

import java.io.PrintStream
import scala.collection.mutable.ListBuffer

object Logger:
    private type Event = String // TODO: se non crei istanze di tipo Event, rimetti string.

    private val savedEventsList = ListBuffer[Event]()   // TODO: ragione per bene se esiste questa lista o se conviene far stampare subito tutto.
    private val prefix = PrefixFormatter()
    private var isEnabled = true
    private val outputMedia: PrintStream = System.out   //PrintStream("debug.txt")

    def disable(): Unit = isEnabled = false

    def enable(): Unit = isEnabled = true

    def log(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        outputMedia.println(event)

    def save(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        savedEventsList += (prefix.getStandardPrefixWithCurrentTime + event)

    def logAll(): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        savedEventsList.foreach(e => Logger.log(e))
        Logger.reset()

    def getSize: Int = savedEventsList.size

    def reset(): Unit = savedEventsList.clear()