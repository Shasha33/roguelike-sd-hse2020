package event

import data.*
import data.Unit

class WorldSimulation {
    fun addEvents(eventBus: EventBus) {
        eventBus.addEvent(NewLevelEvent())
        eventBus.addEvent(PickClewEvent())
        eventBus.addEvent(DieEvent())
    }
}

class NewLevelEvent : Event {
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

class PickClewEvent : Event {
    override fun apply(context: Context): EventResult {
        val playerPoint = context.getPlayerPoint() ?: return EventResult(ExitCode.EXIT, "No player found on the map")
        val pickable = context.getTypeObjectAt(Pickable::class, playerPoint) ?: return EventResult(ExitCode.CONTINUE)
        context.removeObject(Pickable::class, playerPoint)
        val player = context.getPlayer() ?: return EventResult(ExitCode.EXIT, "No player found on the map")

        when(pickable) {
            is Clew -> player.addClew()
            is Hat -> {
                val uselessItem = player.pickHat(pickable)
                context.addObject(uselessItem, playerPoint)
            }
            is Sword -> {
              val uselessItem = player.pickSword(pickable)
                context.addObject(uselessItem, playerPoint)
            }
        }

        return EventResult(ExitCode.CONTINUE)
    }
}

class DieEvent : Event {
    override fun apply(context: Context): EventResult {
        context.getMap().forEach {
            for (gameObject in it.value) {
                if (gameObject is Unit && !gameObject.isAlive()) {
                    context.removeObject(Unit::class, it.key)
                    if (gameObject is Player) {
                        return EventResult(ExitCode.EXIT, "player is already dead")
                    }
                }
            }
        }
        return EventResult(ExitCode.CONTINUE)
    }
}