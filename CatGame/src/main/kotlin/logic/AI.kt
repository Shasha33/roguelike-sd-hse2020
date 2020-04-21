package logic

import data.Context
import data.Enemy
import event.Event

class AI {
    fun aiAction(context: Context): List<Event> {
        val enemies = context.getMap().values.flatten().filterIsInstance<Enemy>()
        return enemies.map { it.strategy.createTurn(context, it) }
    }
}