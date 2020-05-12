package controller

import data.Context
import event.ExitCode
import event.PlayerEvent
import grpc.KekServer
import kotlinx.coroutines.flow.toList
import logic.GameManager
import logic.PlayerAction
import logic.PlayerActions
import ru.hse.kek.Roguelike
import tornadofx.Controller
import java.util.concurrent.Flow
import views.LevelView
import java.util.concurrent.LinkedBlockingQueue

class MainController(customGameManager: GameManager? = null) : Controller() {
    private val channel = LinkedBlockingQueue<PlayerAction>()
    private val gameManager = customGameManager ?: GameManager()
    private lateinit var clientWrapper : ClientWrapper
    private lateinit var server : KekServer

    fun addToActionQueue(button: String, playerId: Int = 0) {
        val context = getContext()
        val action = when (button) {
            "UP" -> PlayerActions(context, playerId).moveUp()
            "DOWN" -> PlayerActions(context, playerId).moveDown()
            "LEFT" -> PlayerActions(context, playerId).moveLeft()
            "RIGHT" -> PlayerActions(context, playerId).moveRight()
            else -> return
        }
        channel.add(action)
    }

    fun runAsServer(port: Int) {
        server = KekServer(port) {
            for (session in it) {
                println("session! ${session.name}")
            }
        }
        server.start()
        server.awaitTermination()
    }

    fun serverShutdown() {
        server.close()
    }

//    fun connectToServer(host: String, port: Int) {
//        clientWrapper = ClientWrapper(host, port)
//        return clientWrapper.client.getSessionsList()
//    }

    fun getContext(): Context {
        return gameManager.context
    }

    fun saveContext(saveDir: String) {
        gameManager.saveGame(getContext(), saveDir)
    }

    fun stopGame() {
        channel.add(PlayerActions(getContext()).stopGame())
    }

    fun startGame(path: String? = null) {
        gameManager.generateNewLevel(path)
         gameManager.context.addReaction { ctx ->
             tornadofx.runLater { find<LevelView>().update(ctx) }
         }
        runAsync {
            loop@ while (true) {
                val exitCode = gameManager.runLevel(listOf(PlayerEvent(channel)))
                when (exitCode) {
                    ExitCode.GO_DOWN, ExitCode.GO_UP -> gameManager.generateNewLevel()
                        ?: println("New level generation failed")
                    ExitCode.EXIT -> break@loop // you died
                    ExitCode.GAME_OVER -> {
                        gameManager.deleteSave()
                    }
                    else -> println("AAA") //should not happen
                }
            }
        }
    }

    fun startServer(port: String) {
        TODO("Not yet implemented")
    }

    fun connectTo(ip: String, port: String) {
        TODO("Not yet implemented")
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