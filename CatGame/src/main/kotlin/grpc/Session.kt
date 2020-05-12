package grpc

import controller.MainController
import data.Context
import data.GameObject
import data.Player
import data.Point
import logic.GameManager
import logic.LevelProducer
import kotlin.random.Random

class Session(val id : Int) {
    private val players = mutableListOf<Player>()
    private val levelProducer = LevelProducer()
    val context: Context
        get() = gameController.getContext()
    private val gameController = MainController()

    fun addPlayer(): Int {
        val index = players.size
        val player = Player(index) {
            removePlayer(index)
        }
        players.add(player)
        val point = context.getRandomEmptyPoint() ?: return -1
        context.addObject(player, point)
        return index
    }

    fun addPlayerMove(playerId: Int, button: String) {
        gameController.addToActionQueue(button, playerId)
    }

    fun removePlayer(playerId: Int) {
        val playerPoint = context.getPlayerPoint(playerId) ?: return
        context.removeObject(Player::class, playerPoint)
        players.removeAt(playerId)
    }

    fun start() {
        gameController.startGame()
    }

    inner class NetworkGameManager : GameManager() {
        override fun getNewLevel(): Context? {
            val context = levelProducer.create(Random.nextInt())
            for (player in players) {
                val newPoint = context.getRandomEmptyPoint() ?: return null
                context.addObject(player, newPoint)
            }
            return context
        }

        override fun contextUpdateReaction(): (Map<Point, List<GameObject>>) -> Unit {
            return {}
        }

    }
}