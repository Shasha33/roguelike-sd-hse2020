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
        if (context.containsClass(Clew::class, playerPoint)) {
            context.removeObject(Clew::class, playerPoint)
            val player = context.getPlayer()
            player?.addClew()
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
                }
            }
        }
        return EventResult(ExitCode.CONTINUE)
    }
}