package com.sd.roguelike.logic

import com.sd.roguelike.data.Context
import com.sd.roguelike.event.Event
import com.sd.roguelike.event.EventBus
import com.sd.roguelike.event.ExitCode
import com.sd.roguelike.event.WorldSimulation

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