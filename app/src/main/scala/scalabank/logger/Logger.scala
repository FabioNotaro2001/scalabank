package scalabank.logger

import java.io.PrintStream
import scala.collection.mutable.ListBuffer
import scalabank.logger.PrefixFormatter

object Logger:
    private type Event = String // TODO: se non crei istanze di tipo Event, rimetti string e togli questo type.
    private var isEnabled = true
    private var outputMedia: PrintStream = System.out

    def disable(): Unit = isEnabled = false

    def enable(): Unit = isEnabled = true

    // TODO: aggiungere come parametro di log il prefix [CREATION], [DEPOSIT], [WITHDRAW]...
    def log(event: Event): Unit =
        require(isEnabled, "The logger is not currently enabled!")
        outputMedia.println(PrefixFormatter.getStandardPrefixWithCurrentTime + event)

    def setOutputMediaToFile(fileName: String): Unit = outputMedia = PrintStream(fileName + ".txt")
    def setOutputMediaToConsole(): Unit = outputMedia = System.out