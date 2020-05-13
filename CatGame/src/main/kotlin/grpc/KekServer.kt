package grpc

import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import logic.LevelLoader
import ru.hse.kek.KekGrpcKt
import ru.hse.kek.Roguelike
import java.lang.Exception

class KekServer(private val port: Int) {
    private val sessions = mutableListOf<Session>()
    private val levelLoader = LevelLoader()
    private val server: Server
    private var reaction : (List<Session>) -> Unit = {}

    constructor(port: Int, stateChangeReaction: (List<Session>) -> Unit) : this(port) {
        reaction = stateChangeReaction
    }

    init {
        server = ServerBuilder.forPort(port).addService(KekService()).build()
    }

    fun addSession(name: String): Int {
        val id = sessions.size
        sessions.add(Session(id, name))
        reaction(sessions)
        return id
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

    private inner class KekService : KekGrpcKt.KekCoroutineImplBase() {
        override suspend fun createSession(request: Roguelike.SessionInitInfo): Roguelike.Session {
            val sessionId = addSession(request.name)
            return Roguelike.Session.newBuilder().apply {
                id = sessionId
                name = request.name
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
            return sessions.map { Roguelike.Session.newBuilder().apply {
                id = it.id
                name = it.name
            }.build() }.asFlow()
        }

        override suspend fun makeTurn(request: Roguelike.PlayerTurn): Roguelike.ContextState {
            val sessionId = request.player.session.id
            val playerId = request.player.number
            val button = request.button

            sessions[sessionId].addPlayerMove(playerId, button)
            return updateMap(request.player)
        }

        override suspend fun updateMap(request: Roguelike.PlayerId): Roguelike.ContextState {
            try {
                println("update")
                val sessionId = request.session.id
                println("1")
                val currentContext = sessions[sessionId].context
                println("2")
                val packedContext = levelLoader.packContext(currentContext)
                println("3 $packedContext")
                return Roguelike.ContextState.newBuilder().setSerializedMap(packedContext).build()
            } catch (e: Exception) {
                println(e)
                return Roguelike.ContextState.newBuilder().setSerializedMap("{}").build()
            }
        }
    }
}