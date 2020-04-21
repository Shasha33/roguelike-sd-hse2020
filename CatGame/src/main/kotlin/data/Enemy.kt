package data

import event.Event
import event.EventResult
import event.ExitCode

class Enemy(val strategy: EnemyStrategy) : Unit() {
    override val strength = 5
    override var hp = 50
}

interface EnemyStrategy {
    fun createTurn(context: Context, unit: Unit): Event
}

class PassiveStrategy() : EnemyStrategy {
    override fun createTurn(context: Context, unit: Unit): Event {
        return object : Event {
            override fun apply(context: Context): EventResult {
                return EventResult(ExitCode.CONTINUE)
            }
        }
    }
}

class PassiveAggressiveStrategy() : EnemyStrategy {
    override fun createTurn(context: Context, unit: Unit): Event {
        return object : Event {
            override fun apply(context: Context): EventResult {
                val player = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
                val point = context.getPointByObject(unit) ?: return EventResult(ExitCode.EXIT, "No unit found on the map")
                val possibleMoves = mutableListOf<Point>()
                if (point.x < player.x) {
                       possibleMoves.add(Point(point.x - 1, point.y))
                } else if (point.x > player.x) {
                    possibleMoves.add(Point(point.x - 1, point.y))
                }

                if (point.y < player.y) {
                    possibleMoves.add(Point(point.x, point.y - 1))
                } else if (point.y > player.y) {
                    possibleMoves.add(Point(point.x, point.y + 1))
                }

                for (p in possibleMoves) {
                    context.moveObject(Enemy::class, point, p)
                }

                return EventResult(ExitCode.CONTINUE)
            }

        }
    }
}