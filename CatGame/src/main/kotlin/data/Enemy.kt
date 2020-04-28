package data

import event.Event
import event.EventResult
import event.ExitCode
import kotlin.math.abs

class Enemy(var strategy: EnemyStrategy) : Unit {
    override val stats = Stats(50, 5, 0)
}

interface EnemyStrategy {
    fun createTurn(context: Context, unit: Unit): Event
}

class StrategyDecorator(private val strategy: EnemyStrategy) : EnemyStrategy {
    private val randomStrategy = ContusedStrategy()
    private var confusedMoves = 5

    override fun createTurn(context: Context, unit: Unit): Event {
        return if (confusedMoves > 0) {
            confusedMoves--
            randomStrategy.createTurn(context, unit)
        } else {
            strategy.createTurn(context, unit)
        }
    }
}

class ContusedStrategy : EnemyStrategy {
    override fun createTurn(context: Context, unit: Unit): Event {
        return object : Event {
            override fun apply(context: Context): EventResult {
                val point = context.getPointByObject(unit) ?: return EventResult(ExitCode.EXIT, "No unit found on the map")
                val (dx, dy) = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1).random()
                context.moveObject(unit, point, Point(point.x + dx, point.y + dy))
                return EventResult(ExitCode.CONTINUE)
            }
        }
    }
}

class PassiveStrategy : EnemyStrategy {
    override fun createTurn(context: Context, unit: Unit): Event {
        return object : Event {
            override fun apply(context: Context): EventResult {
                return EventResult(ExitCode.CONTINUE)
            }
        }
    }
}

class PassiveAggressiveStrategy : EnemyStrategy {
    override fun createTurn(context: Context, unit: Unit): Event {
        return object : Event {
            override fun apply(context: Context): EventResult {
                val player = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
                val point = context.getPointByObject(unit) ?: return EventResult(ExitCode.EXIT, "No unit found on the map")
                val possibleMoves = mutableListOf<Point>()
                if (point.x < player.x) {
                       possibleMoves.add(Point(point.x - 1, point.y))
                } else if (point.x > player.x) {
                    possibleMoves.add(Point(point.x + 1, point.y))
                }

                if (point.y < player.y) {
                    possibleMoves.add(Point(point.x, point.y - 1))
                } else if (point.y > player.y) {
                    possibleMoves.add(Point(point.x, point.y + 1))
                }

                for (p in possibleMoves) {
                    context.moveObject(unit, point, p)
                }

                return EventResult(ExitCode.CONTINUE)
            }
        }
    }
}

class AggressiveStrategy : EnemyStrategy {
    override fun createTurn(context: Context, unit: Unit): Event {
        return object : Event {
            override fun apply(context: Context): EventResult {
                val player = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
                val point = context.getPointByObject(unit) ?: return EventResult(ExitCode.EXIT, "No unit found on the map")

                if (abs(player.x - point.x) + abs(player.y - point.y) == 1) {
                    val playerObject = context.getPlayer() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
                    playerObject.damage(unit.damageValue)
                    context.stepsCount++
                    unit.damage(playerObject.damageValue)
                    context.stepsCount++
                    return EventResult(ExitCode.CONTINUE)
                }

                val possibleMoves = possibleMoves(point, player)
                for (p in possibleMoves) {
                    context.moveObject(unit, point, p)
                }

                return EventResult(ExitCode.CONTINUE)
            }
        }
    }

    private fun possibleMoves(point: Point, player: Point): List<Point> {
        val possibleMoves = mutableListOf<Point>()

        if (point.x < player.x) {
            possibleMoves.add(Point(point.x + 1, point.y))
        } else if (point.x > player.x) {
            possibleMoves.add(Point(point.x - 1, point.y))
        }

        if (point.y < player.y) {
            possibleMoves.add(Point(point.x, point.y + 1))
        } else if (point.y > player.y) {
            possibleMoves.add(Point(point.x, point.y - 1))
        }
        return possibleMoves
    }

}