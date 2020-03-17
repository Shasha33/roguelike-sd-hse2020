package logic

import data.Context
import event.EventBus
import event.EventResult
import event.ExitCode
import event.WorldSimulation

class GameLoop(private val context: Context) {
    fun run() {
        val worldSimulation = WorldSimulation()
        val eventBus = EventBus()
        worldSimulation.addEvents(eventBus)

        var result = EventResult(ExitCode.CONTINUE)
        while (result.exitCode == ExitCode.CONTINUE) {
            result = eventBus.callAll(context)
        }
    }
}