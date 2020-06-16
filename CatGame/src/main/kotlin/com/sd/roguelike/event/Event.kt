package  com.sd.roguelike.event

import com.sd.roguelike.data.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.random.Random

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
        val player = context.getPlayer() ?: return EventResult(ExitCode.EXIT)
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT)
        val newPoint = when (element) {
            "UP" -> Point(playerPoint.x, playerPoint.y - 1)
            "DOWN" -> Point(playerPoint.x, playerPoint.y + 1)
            "LEFT" -> Point(playerPoint.x - 1, playerPoint.y)
            "RIGHT" -> Point(playerPoint.x + 1, playerPoint.y)
            else -> return EventResult(ExitCode.CONTINUE)
        }

        if (context.containsClass(Enemy::class, newPoint)) {
            attack(player, context, newPoint)
        } else if (!context.isWall(newPoint)) {
            context.moveObject(player, playerPoint, newPoint)
        }
        return EventResult(ExitCode.CONTINUE)
    }

    private fun attack(player: Player, context: Context, enemyPoint: Point) {
        val enemy = context.getTypeObjectAt(Enemy::class, enemyPoint) as? Enemy ?: return

        if (Random.nextInt(10) == 1) {
            enemy.strategy = StrategyDecorator(enemy.strategy)
            return
        }

        enemy.damage(player.damageValue)
        context.stepsCount++
        player.damage(enemy.damageValue)
        context.stepsCount++
    }
}

enum class ExitCode {
    CONTINUE, GO_UP, GO_DOWN, EXIT
}