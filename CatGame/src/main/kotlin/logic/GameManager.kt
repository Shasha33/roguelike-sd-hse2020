package logic

import data.Context
import data.GameObject
import data.Player
import data.Point
import event.Event
import event.ExitCode
import tornadofx.find
import views.LevelView
import kotlin.random.Random

open class GameManager {
    private val levelLoader = LevelLoader()
    private val levelProducer = LevelProducer()
    private val gameFactory = GameFactory()
    var context: Context = Context(0, 0)
        private set

    open fun contextUpdateReaction(): (Map<Point, List<GameObject>>) -> Unit {
        return {
            tornadofx.runLater { find<LevelView>().update(it) }
        }
    }

    open fun getNewLevel(): Context? {
        val context = createLevel(Random.nextInt())
        val point = context.getRandomEmptyPoint() ?: return null
        context.addObject(Player(), point)
        return context
    }

    fun generateNewLevel(path: String? = null): Context? {
        val newContext = if (path != null) createLevel(path) else getNewLevel()
        context = newContext ?: return null
        return context
    }

    fun createLevel(seed: Int): Context {
        return levelProducer.create(seed)
    }

    fun createLevel(path: String): Context? {
        return levelLoader.loadFrom(path)
    }

    fun saveGame(context: Context, path: String) {
        levelLoader.saveTo(context, path)
    }

    fun runLevel(eventList: List<Event> = emptyList()): ExitCode {
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