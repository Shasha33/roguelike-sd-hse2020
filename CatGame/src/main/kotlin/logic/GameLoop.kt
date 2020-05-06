package logic

import data.Context
import event.*

class GameLoop(private val context: Context, private val eventBus : EventBus) {
    fun run(): ExitCode {
        var result = EventResult(ExitCode.CONTINUE)
        while (result.exitCode == ExitCode.CONTINUE) {
            result = eventBus.callAll(context)
        }
        return result.exitCode
    }
}