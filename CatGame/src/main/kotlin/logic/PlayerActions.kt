package logic

import data.*
import event.EventResult
import event.ExitCode
import kotlin.random.Random

open class PlayerAction(private val runnable: () -> EventResult) {
    fun call(): EventResult {
        return runnable.invoke()
    }
}

class PlayerActions(private val context: Context) {
     private fun apply(delta: Point): EventResult {
        val player = context.getPlayer() ?: return EventResult(ExitCode.EXIT)
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT)
        val newPoint = playerPoint.add(delta)

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

    fun moveRight(): PlayerAction {
        return PlayerAction { apply(Point(1, 0)) }
    }

    fun moveLeft(): PlayerAction {
        return PlayerAction { apply(Point(-1, 0)) }
    }

    fun moveUp(): PlayerAction {
        return PlayerAction { apply(Point(0, -1)) }
    }

    fun moveDown(): PlayerAction {
        return PlayerAction { apply(Point(0, 1)) }
    }

    fun stopGame(): PlayerAction {
        return PlayerAction { EventResult(ExitCode.EXIT) }
    }
}