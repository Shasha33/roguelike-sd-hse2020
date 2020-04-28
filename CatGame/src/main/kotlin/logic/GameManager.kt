package logic

import data.Context
import event.Event
import event.ExitCode
import java.io.File

class GameManager {
    private val levelLoader = LevelLoader()
    private val levelProducer = LevelProducer()
    private val gameFactory = GameFactory()

    fun createLevel(seed: Int): Context {
        return levelProducer.create(seed)
    }

    fun createLevel(path: String): Context? {
        return levelLoader.loadFrom(path)
    }

    fun saveGame(context: Context, path: String) {
        levelLoader.saveTo(context, path)
    }

    fun runLevel(context: Context, eventList: List<Event> = emptyList()): ExitCode {
//        val gameLoop = gameFactory.createGame(context)
        val gameLoop = GameLoop(context, eventList)
        val exitCode = gameLoop.run()
        if (exitCode != ExitCode.EXIT) {
            levelLoader.nextLevel(context)
        }
        return exitCode
    }

    fun deleteSave() {
        levelLoader.dropLastSave()
    }
}