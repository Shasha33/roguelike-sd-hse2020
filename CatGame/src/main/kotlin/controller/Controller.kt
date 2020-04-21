package controller

import data.Context
import data.Player
import data.Point
import event.PlayerEvent
import logic.GameManager
import tornadofx.Controller
import views.LevelView
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.random.Random

class MainController : Controller() {
    var usePath: File? = null
    private val channel = ConcurrentLinkedQueue<String>()
    private val gameManager = GameManager()
    private var context = gameManager.createLevel(Random.nextInt())

    fun addToActionQueue(button: String) {
        channel.add(button)
    }

    fun getContext(): Context {
        return context
    }

    fun saveContext(saveDir: File) {
        TODO()
    }

    fun runGame() {
        context = gameManager.createLevel(Random.nextInt())
        runAsync {
            context.addReaction {
                //TODO call level update
                tornadofx.runLater { find<LevelView>().update(it) }
            }
            val loop = gameManager.runLevel(context, listOf(PlayerEvent(channel)))
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