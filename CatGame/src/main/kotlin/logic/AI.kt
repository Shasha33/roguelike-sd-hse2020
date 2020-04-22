package logic

import data.AggressiveStrategy
import data.Context
import data.Enemy
import data.PassiveStrategy
import event.Event

class AI {
    private val distance = 10

    fun aiAction(context: Context): List<Event> {
        val enemies = context.getMap().filter { it.value.any { gameObject ->  gameObject is Enemy} }
        val player = context.getPlayerPoint()
        for (enemy in enemies) {
            val point = enemy.key
            val gameObject = enemy.value.filterIsInstance<Enemy>().singleOrNull() ?: continue
            if (player?.dist(point) ?: 0 < distance && gameObject.strategy is PassiveStrategy) {
                gameObject.strategy = AggressiveStrategy()
            } else if (player?.dist(point) ?: 0 >= distance && gameObject.strategy is AggressiveStrategy) {
                gameObject.strategy = PassiveStrategy()
            }
        }

        return enemies.flatMap {
            it.value.filterIsInstance<Enemy>().map { enemy -> enemy.strategy.createTurn(context, enemy) }
        }
    }
}