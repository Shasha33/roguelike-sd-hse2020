package com.sd.roguelike.logic

import com.sd.roguelike.data.Context
import com.sd.roguelike.event.Event
import com.sd.roguelike.event.ExitCode

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
//        val gameLoop = gameFactory.createGame(context)
        val gameLoop = GameLoop(context, eventList)
        return gameLoop.run()
    }
}