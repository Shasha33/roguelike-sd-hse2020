package event

import data.Context

interface Event {
    fun apply(context: Context): EventResult
}

class EventResult(val exitCode: ExitCode, val message: String = "")

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT, GAME_OVER
}

class EventBus {
    private val events = mutableListOf<Event>()

    fun addEvent(event: Event) {
        events.add(event)
    }

    fun removeEvent(event: Event) {
        events.remove(event)
    }

    fun addAll(events: List<Event>) {
        this.events.addAll(events)
    }

    fun callAll(context: Context): EventResult {
        for (event in events) {
            val result = event.apply(context)
            if (result.exitCode != ExitCode.CONTINUE) {
                return result
            }
        }
        return EventResult(ExitCode.CONTINUE)
    }
}