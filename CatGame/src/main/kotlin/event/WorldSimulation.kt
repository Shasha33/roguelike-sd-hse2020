package event

import data.Clew
import data.Context
import data.DoorDown
import data.DoorUp

class WorldSimulation {
    fun addEvents(eventBus: EventBus) {
        eventBus.addEvent(NewLevelEvent())
        eventBus.addEvent(PickClewEvent())
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