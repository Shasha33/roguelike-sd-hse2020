package event

import data.*
import logic.PlayerAction
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class PlayerEvent(private val queue: LinkedBlockingQueue<PlayerAction>) : Event {
    override fun apply(context: Context): EventResult {
        val action = queue.poll() ?: return EventResult(ExitCode.CONTINUE)
        return action.call()
    }
}
