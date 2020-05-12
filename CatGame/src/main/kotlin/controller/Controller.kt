package controller

import data.Context
import data.GameObject
import data.Point
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

class MainController(createdContext: Context? = null) : Controller() {
    private val channel = LinkedBlockingQueue<PlayerAction>()
    private val gameManager = GameManager()
    private var context: Context = createdContext ?: Context(0, 0)
        set(newContext) {
            newContext.addReaction {
                //TODO call level update
                tornadofx.runLater { find<LevelView>().update(it) }
            }
            field = newContext
        }

    fun addToActionQueue(button: String, playerId : Int = 0) {
        val action = when(button) {
            "UP" -> PlayerActions(context, playerId).moveUp()
            "DOWN" -> PlayerActions(context, playerId).moveDown()
            "LEFT" -> PlayerActions(context, playerId).moveLeft()
            "RIGHT" -> PlayerActions(context, playerId).moveRight()
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

        run() {
            //TODO call level update
            tornadofx.runLater { find<LevelView>().update(it) }
        }
    }

    //to replace ui update with sending new state to all players
    fun run(reaction: (Map<Point, List<GameObject>>) -> Unit) {
        context.addReaction {
            reaction(it)
        }
        runAsync {
            loop@ while (true) {
                val exitCode = gameManager.runLevel(context, listOf(PlayerEvent(channel)))
                when (exitCode) {
                    ExitCode.GO_DOWN, ExitCode.GO_UP -> context = gameManager.createLevel(Random.nextInt())
                    ExitCode.EXIT -> break@loop // you died
                    ExitCode.GAME_OVER -> {
                        gameManager.deleteSave()
                    }
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