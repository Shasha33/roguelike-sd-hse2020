package event

import data.*

interface Event {
    fun apply(context: Context): EventResult
}

class EventResult(val exitCode: ExitCode, val message: String = "")


class PlayerEvent(private val queue: String) : Event { // TODO tut
    override fun apply(context: Context): EventResult {
        return EventResult(ExitCode.EXIT)
    }
}

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT
}