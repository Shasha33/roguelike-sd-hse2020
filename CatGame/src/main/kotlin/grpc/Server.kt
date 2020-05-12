package grpc

import controller.MainController
import data.Context
import data.GameObject
import data.Player
import data.Point
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import logic.LevelLoader
import logic.LevelProducer
import ru.hse.kek.KekGrpcKt
import ru.hse.kek.Roguelike

class KekServer(private val port: Int) {
    private val sessions = mutableListOf<Session>()
    private val levelProducer = LevelProducer()
    private val levelLoader = LevelLoader()
    private val server: Server

    init {
        server = ServerBuilder.forPort(port).addService(KekService()).build()
    }

    fun addSession(): Int {
        val id = sessions.size
        val context = levelProducer.create(42)
        sessions.add(Session(id, context))
        return id
    }

    fun addNewPlayerToSession(id: Int): Int {
        return sessions[id].addPlayer()
    }

    fun start() {
        server.start()
    }

    fun awaitTermination() {
        server.awaitTermination()
    }

    fun close() {
        server.shutdown()
    }

    inner class KekService : KekGrpcKt.KekCoroutineImplBase() {
        override suspend fun createSession(request: Roguelike.SessionInitInfo): Roguelike.Session {
            val sessionId = addSession()
            return Roguelike.Session.newBuilder().apply {
                id = sessionId
            }.build()
        }

        override suspend fun addToSession(request: Roguelike.Session): Roguelike.PlayerId {
            val sessionId = request.id
            val playerId = sessions[sessionId].addPlayer()
            return Roguelike.PlayerId.newBuilder().apply {
                number = playerId
                session = Roguelike.Session.newBuilder().setId(sessionId).build()

            }.build()
        }

        override suspend fun disconnect(request: Roguelike.PlayerId): Roguelike.NyanMessage {
            val sessionId = request.session.id
            val playerId = request.number
            sessions[sessionId].removePlayer(playerId)
            return Roguelike.NyanMessage.newBuilder().setText("Goodbye").build()
        }

        override fun sessionsList(request: Roguelike.NyanMessage): Flow<Roguelike.Session> {
            val messageText = request.text
            return sessions.map { Roguelike.Session.newBuilder().setId(it.id).build() }.asFlow()
        }

        override suspend fun makeTurn(request: Roguelike.PlayerTurn): Roguelike.ContextState {
            val sessionId = request.player.session.id
            val playerId = request.player.number
            val button = request.button

            sessions[sessionId].addPlayerMove(playerId, button)
            return updateMap(request.player)
        }

        override suspend fun updateMap(request: Roguelike.PlayerId): Roguelike.ContextState {
            val sessionId = request.session.id
            val currentContext = sessions[sessionId].context
            val packedContext = levelLoader.packContext(currentContext)
            return Roguelike.ContextState.newBuilder().setSerializedMap(packedContext).build()
        }
    }
}

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