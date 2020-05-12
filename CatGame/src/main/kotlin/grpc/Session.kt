package grpc

import controller.MainController
import data.Context
import data.GameObject
import data.Player
import data.Point

class Session(val id : Int, val context: Context) {
    private val players = mutableListOf<Player>()
    private val gameController = MainController(context)

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

    fun start(reaction: (Map<Point, List<GameObject>>) -> Unit) {
        gameController.run(reaction)
    }

}