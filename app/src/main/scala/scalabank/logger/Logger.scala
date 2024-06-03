package scalabank.logger

import java.io.PrintStream

trait Logger:
    def disable(): Unit
    def enable(): Unit
    def log(event: String): Unit
    def setOutputMediaToFile(fileName: String): Unit
    def setOutputMediaToConsole(): Unit

trait LoggerDependency:
    val logger: Logger

class LoggerImpl extends Logger:
    private var isEnabled = true
    private var outputMedia: PrintStream = System.out

    def disable(): Unit = isEnabled = false
    def enable(): Unit = isEnabled = true

    def log(event: String): Unit =
        if isEnabled then outputMedia.println(PrefixFormatter.getStandardPrefixWithCurrentTime + event)

    def setOutputMediaToFile(fileName: String): Unit = outputMedia = PrintStream(fileName + ".txt")
    def setOutputMediaToConsole(): Unit = outputMedia = System.out
