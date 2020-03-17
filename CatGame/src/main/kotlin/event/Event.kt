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

class NewLevelEvent() : Event {
    override fun apply(context: Context): EventResult {
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
        if (context.containsClass(DoorDown::class, playerPoint)) {
            return EventResult(ExitCode.GO_DOWN)
        }
        if (context.containsClass(DoorUp::class, playerPoint)) {
            return EventResult(ExitCode.GO_UP)
        }
        return EventResult(ExitCode.CONTINUE)
    }
}

class PickClewEvent() : Event {
    override fun apply(context: Context): EventResult {
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
        if (context.containsClass(Clew::class, playerPoint)) {
            context.removeObject(Clew::class, playerPoint)
            val player = context.getPlayer()
            player?.addClew()
        }
        return EventResult(ExitCode.CONTINUE)
    }
}

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT
}