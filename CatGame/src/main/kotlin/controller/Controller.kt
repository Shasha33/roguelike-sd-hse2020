package controller

import data.Context
import data.GameObject
import data.Point
import javafx.beans.property.MapProperty
import javafx.beans.property.SimpleMapProperty
import javafx.collections.ObservableMap
import logic.GameManager
import tornadofx.Controller
import tornadofx.ItemViewModel
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.random.Random

class MainController: Controller() {
    var usePath: File? = null
    private val channel = ConcurrentLinkedQueue<String>()
    private val gameManager = GameManager()

    fun addToActionQueue(button:String) {
        channel.add(button)
    }
    fun getContext(): Context? {
        TODO()
    }

    fun saveContext(saveDir: File) {
        TODO()
    }

    fun runGame() {
        runAsync {
            val lvl = gameManager.createLevel(Random.nextInt())
            val loop = gameManager.runLevel(lvl)
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