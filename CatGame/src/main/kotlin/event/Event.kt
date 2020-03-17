package event

import data.Context
import data.DoorDown
import data.DoorUp
import data.Player

interface Event {
    fun apply(context: Context): EventResult
}

class EventResult(val exitCode: ExitCode, val message: String = "")


class PlayerEvent(private val queue: String) : Event { // TODO tut
    override fun apply(context: Context): EventResult {

    }
}

class NewLevelEvent() : Event {
    override fun apply(context: Context): EventResult {
        val doorUpPoint = context.getUniqueObjectPoint(DoorUp::class)
        val doorDownPoint = context.getUniqueObjectPoint(DoorDown::class)
        val playerPoint = context.getUniqueObjectPoint(Player::class)

        if (doorDownPoint == playerPoint) {
            return EventResult(ExitCode.GO_DOWN)
        }
        if (doorUpPoint == playerPoint) {
            return EventResult(ExitCode.GO_UP)
        }
        return EventResult(ExitCode.CONTINUE)
    }
}

class PickClewEvent() : Event {
    override fun apply(context: Context): EventResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT
}