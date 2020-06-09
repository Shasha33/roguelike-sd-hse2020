package com.sd.roguelike.event

import com.sd.roguelike.data.*

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
                if (gameObject is com.sd.roguelike.data.Unit && !gameObject.isAlive()) {
                    context.removeObject(com.sd.roguelike.data.Unit::class, it.key)
                    if (gameObject is Player) {
                        return EventResult(ExitCode.EXIT, "player is already dead")
                    }
                }
            }
        }
        return EventResult(ExitCode.CONTINUE)
    }
}