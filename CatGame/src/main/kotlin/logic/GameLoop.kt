package logic

import data.Context
import event.*

class GameLoop(private val context: Context, private val eventList : List<Event>) {
    fun run() {
        val worldSimulation = WorldSimulation()
        val eventBus = EventBus()
        worldSimulation.addEvents(eventBus)
        for (event in eventList) {
            eventBus.addEvent(event)
        }

        var result = EventResult(ExitCode.CONTINUE)
        while (result.exitCode == ExitCode.CONTINUE) {
            result = eventBus.callAll(context)
        }
    }
}