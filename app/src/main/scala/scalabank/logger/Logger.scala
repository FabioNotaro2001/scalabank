package scalabank.logger

object Logger:
    type Event = String

    private var savedEventsList = LazyList[Event]()
    private val prefix: String = PrefixFormatter().getStandardPrefixFormatter

    def log(event: Event): Unit = println(event)
    def save(event: Event): Unit = savedEventsList = savedEventsList.prepended(prefix + event)
    def getSize(): Int = savedEventsList.size
    def reset(): Unit = savedEventsList = LazyList[Event]()
    def logAll(): Unit =
        savedEventsList.foreach(e => Logger.log(e))
        Logger.reset()