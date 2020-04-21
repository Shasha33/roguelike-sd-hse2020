package event

import data.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

interface Event {
    fun apply(context: Context): EventResult
}

class EventResult(val exitCode: ExitCode, val message: String = "")


class PlayerEvent(private val queue: LinkedBlockingQueue<String>) : Event {
    override fun apply(context: Context): EventResult {
        val element = queue.poll(1, TimeUnit.DAYS) ?: "null"
        if (element == "ItsTimeToStop") { // TODO: heh
            return EventResult(ExitCode.EXIT)
        }
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT)
        val newPoint = when(element) {
            "UP" -> Point(playerPoint.x, playerPoint.y - 1)
            "DOWN" -> Point(playerPoint.x, playerPoint.y + 1)
            "LEFT" -> Point(playerPoint.x - 1, playerPoint.y)
            "RIGHT" -> Point(playerPoint.x + 1, playerPoint.y)
            else -> return EventResult(ExitCode.CONTINUE)
        }

        if (context.containsClass(Enemy::class, newPoint)) {
            attack(context, newPoint)
        } else if (!context.isWall(newPoint)) {
            context.moveObject(Player::class, playerPoint, newPoint)
        }
        return EventResult(ExitCode.CONTINUE)
    }

    private fun attack(context: Context, enemyPoint: Point) {
        val player = context.getPlayer() ?: return
        val enemy = context.getTypeObjectAt(Enemy::class, enemyPoint) as? Enemy ?: return
        enemy.damage(player.strength)
        player.damage(enemy.strength)
    }
}

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT
}