package controller

import data.Context
import event.ExitCode
import event.PlayerEvent
import logic.GameManager
import logic.PlayerAction
import logic.PlayerActions
import tornadofx.Controller
import views.LevelView
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

class MainController : Controller() {
    private val channel = LinkedBlockingQueue<PlayerAction>()
    private val gameManager = GameManager()
    private var context: Context = Context(0, 0)
        set(newContext) {
            newContext.addReaction {
                //TODO call level update
                tornadofx.runLater { find<LevelView>().update(it) }
            }
            field = newContext
        }

    fun addToActionQueue(button: String) {
        val action = when(button) {
            "UP" -> PlayerActions(context).moveUp()
            "DOWN" -> PlayerActions(context).moveDown()
            "LEFT" -> PlayerActions(context).moveLeft()
            "RIGHT" -> PlayerActions(context).moveRight()
            else -> return
        }
        channel.add(action)
    }

    fun getContext(): Context {
        return context
    }

    fun saveContext(saveDir: String) {
        gameManager.saveGame(context, saveDir)
    }

    fun stopGame() {
        channel.add(PlayerActions(context).stopGame())
    }

    fun startGame(path: String? = null) {
        context = path?.let{ gameManager.createLevel(it)!! } ?: gameManager.createLevel(Random.nextInt())

        context.addReaction {
            //TODO call level update
            tornadofx.runLater { find<LevelView>().update(it) }
        }
        runAsync {
            loop@ while (true) {
                val exitCode = gameManager.runLevel(context, listOf(PlayerEvent(channel)))
                when (exitCode) {
                    ExitCode.GO_DOWN, ExitCode.GO_UP -> context = gameManager.createLevel(Random.nextInt())
                    ExitCode.EXIT -> break@loop // you died
                    else -> println("AAA") //should not happen
                }
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