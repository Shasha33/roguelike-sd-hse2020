package com.sd.roguelike.controller

import com.sd.roguelike.data.Context
import com.sd.roguelike.event.ExitCode
import com.sd.roguelike.event.PlayerEvent
import com.sd.roguelike.logic.GameManager
import com.sd.roguelike.views.LevelView
import tornadofx.Controller
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

/**
 * Auto generated class for ui and logic classes interaction
 */
class MainController : Controller() {
    var usePath: File? = null
    private val channel = LinkedBlockingQueue<String>()
    private val gameManager = GameManager()
    private var context = gameManager.createLevel(Random.nextInt())

    /**
     * adds pressed button
     */
    fun addToActionQueue(button: String) {
        channel.add(button)
    }

    /**
     * returns the current map wrapped to Context class
     */
    fun getContext(): Context {
        return context
    }

    fun saveContext(saveDir: File) {
        TODO()
    }

    fun stopGame() {
        channel.add("ItsTimeToStop")
    }

    fun runGame() {
        context = gameManager.createLevel(Random.nextInt())
        runAsync {
            context.addReaction {
                //TODO call level update
                tornadofx.runLater { find<LevelView>().update(it) }
            }
            val exitCode = gameManager.runLevel(
                context, listOf(
                    PlayerEvent(
                        channel
                    )
                )
            )
            when (exitCode) {
                ExitCode.GO_DOWN, ExitCode.GO_UP -> runGame()
                ExitCode.EXIT -> println("Game Over") // you died
                else -> println("AAA") //should not happen
            }
        }
    }
}