package com.sd.roguelike.logic

import com.sd.roguelike.data.Context
import com.sd.roguelike.event.*

class GameLoop(private val context: Context, private val eventList: List<Event>) {
    fun run(): ExitCode {
        val worldSimulation = WorldSimulation()
        val eventBus = EventBus()
        worldSimulation.addEvents(eventBus)
        eventBus.addAll(eventList)

        val ai = AI()

        var result = EventResult(ExitCode.CONTINUE)
        while (result.exitCode == ExitCode.CONTINUE) {
            result = eventBus.callAll(context)
            for (aiEvent in ai.aiAction(context)) {
                if (result.exitCode == ExitCode.CONTINUE) {
                    result = aiEvent.apply(context)
                }
            }
        }
        return result.exitCode
    }
}