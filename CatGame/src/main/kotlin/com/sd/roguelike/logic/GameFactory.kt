package  com.sd.roguelike.logic

import  com.sd.roguelike.data.Context

class GameFactory {
    fun createGame(context: Context): GameLoop {
        return GameLoop(context, emptyList())
    }
}