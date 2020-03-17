package logic

import data.Context

class GameManager {
    private val levelLoader = LevelLoader()
    private val levelProducer = LevelProducer()
    private val gameFactory = GameFactory()

    fun createLevel(seed: Int): Context {
        return levelProducer.create(seed)
    }

    fun createLevel(path: String): Context {
        return levelLoader.loadFrom(path)
    }

    fun runLevel(context: Context): GameLoop {
//        val gameLoop = gameFactory.createGame(context)
        val gameLoop = GameLoop(context)
        // TODO run
        return gameLoop
    }
}