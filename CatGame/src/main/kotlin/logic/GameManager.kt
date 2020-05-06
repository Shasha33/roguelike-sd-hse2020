package logic

import data.Context
import event.Event
import event.EventBus
import event.ExitCode
import event.WorldSimulation

class GameManager {
    private val levelLoader = LevelLoader()
    private val levelProducer = LevelProducer()

    fun createLevel(seed: Int): Context {
        return levelProducer.create(seed)
    }

    fun createLevel(path: String): Context {
        return levelLoader.loadFrom(path)
    }

    fun runLevel(context: Context, eventList: List<Event> = emptyList()): ExitCode {
        val worldSimulation = WorldSimulation()
        val eventBus = EventBus()
        worldSimulation.addEvents(eventBus)
        for (event in eventList) {
            eventBus.addEvent(event)
        }

        val gameLoop = GameLoop(context, eventBus)
        return gameLoop.run()
    }
}