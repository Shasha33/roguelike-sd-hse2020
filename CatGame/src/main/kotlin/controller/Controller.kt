package controller

import data.Context
import event.ExitCode
import event.PlayerEvent
import logic.GameManager
import tornadofx.Controller
import views.LevelView
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

class MainController : Controller() {
    var usePath: String? = null
    private val channel = LinkedBlockingQueue<String>()
    private val gameManager = GameManager()
    private var context = gameManager.createLevel(Random.nextInt())

    fun addToActionQueue(button: String) {
        channel.add(button)
    }

    fun getContext(): Context {
        return context
    }

    fun saveContext(saveDir: String) {
        gameManager.saveGame(context, saveDir)
    }

    fun stopGame() {
        channel.add("ItsTimeToStop")
    }

    fun runGame() {
        context = if (usePath != null) {
            gameManager.createLevel(usePath!!)!!
        } else {
            gameManager.createLevel(Random.nextInt())
        }
        runAsync {
            context.addReaction {
                //TODO call level update
                tornadofx.runLater { find<LevelView>().update(it) }
            }
            val exitCode = gameManager.runLevel(context, listOf(PlayerEvent(channel)))
            when(exitCode) {
                ExitCode.GO_DOWN, ExitCode.GO_UP -> runGame()
                ExitCode.EXIT -> println("Game Over") // you died
                else -> println("AAA") //should not happen
            }
        }
    }
}

//class ContextModel(ctx: Context) : ItemViewModel<Context>(ctx) {
//    val name = bind(Person::nameProperty)
//    val title = bind(Person::titleProperty)
//}
//
//class ContextProp(ctx: ObservableMap<Point, GameObject>?) {
//    val prop = SimpleMapProperty<Point, GameObject>(this, "ctx", ctx)
//
//}