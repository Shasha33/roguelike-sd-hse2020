package logic

import data.Context

class GameFactory {
    fun createGame(context: Context): GameLoop {
        return GameLoop(context)
    }
}