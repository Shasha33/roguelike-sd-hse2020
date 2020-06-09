package com.sd.roguelike.logic

import com.sd.roguelike.data.Context
import com.sd.roguelike.event.EventBus
import com.sd.roguelike.event.EventResult
import com.sd.roguelike.event.ExitCode

class GameLoop(private val context: Context, private val eventBus: EventBus) {
    fun run(): ExitCode {
        var result =
            EventResult(ExitCode.CONTINUE)
        while (result.exitCode == ExitCode.CONTINUE) {
            result = eventBus.callAll(context)
        }
        return result.exitCode
    }
}