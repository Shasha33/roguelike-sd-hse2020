package controller

import data.Context
import logic.GameManager
import tornadofx.Controller
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