package event

import data.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

interface Event {
    fun apply(context: Context): EventResult
}

class EventResult(val exitCode: ExitCode, val message: String = "")


class PlayerEvent(private val queue: ConcurrentLinkedQueue<String>) : Event {
    override fun apply(context: Context): EventResult {
        val element = queue.poll() ?: "null"
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT)
        val newPoint = when(element) {
            "UP" -> Point(playerPoint.x, playerPoint.y - 1)
            "DOWN" -> Point(playerPoint.x, playerPoint.y + 1)
            "LEFT" -> Point(playerPoint.x - 1, playerPoint.y)
            "RIGHT" -> Point(playerPoint.x + 1, playerPoint.y)
            else -> return EventResult(ExitCode.CONTINUE)
        }
        if (!context.isWall(newPoint)) {
            context.moveObject(Player::class, playerPoint, newPoint)
        }
        return EventResult(ExitCode.CONTINUE)
    }
}

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT
}