package scalabank.logger

import java.io.PrintStream

object Logger:
    private type Event = String // TODO: se non crei istanze di tipo Event, rimetti string e togli questo type.
    private var isEnabled = true
    private var outputMedia: PrintStream = System.out

    def disable(): Unit = isEnabled = false

    def enable(): Unit = isEnabled = true

    def log(event: Event): Unit =
        if isEnabled then outputMedia.println(PrefixFormatter.getStandardPrefixWithCurrentTime + event)

    def setOutputMediaToFile(fileName: String): Unit = outputMedia = PrintStream(fileName + ".txt")
    def setOutputMediaToConsole(): Unit = outputMedia = System.out