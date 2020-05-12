package grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import logic.LevelLoader
import ru.hse.kek.KekGrpcKt
import ru.hse.kek.Roguelike

class Client(host: String, port: Int) {
    private val levelLoader = LevelLoader()
    private val channel: ManagedChannel
    private lateinit var playerId: Roguelike.PlayerId

    init {
        val channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext()
        val dispatcher = Dispatchers.Default
        channel = channelBuilder.executor(dispatcher.asExecutor()).build()
    }

    private val stub = KekGrpcKt.KekCoroutineStub(channel)

    fun getSessionsList() = runBlocking {
        val nyan = Roguelike.NyanMessage.newBuilder().setText("Hello!").build()
        val sessions = stub.sessionsList(nyan)
        sessions
    }

    fun connectToSession(id: Int) = runBlocking {
        val session = Roguelike.Session.newBuilder().setId(id).build()
        playerId = stub.addToSession(session)
        playerId
    }

    fun createNewSessionAndConnect() = runBlocking {
        val sessionInfo = Roguelike.SessionInitInfo.newBuilder().setIsRandom(true).build()
        val session = stub.createSession(sessionInfo)
        playerId = stub.addToSession(session)
    }

    fun makeMove(pressedButton: String) = runBlocking {
        val moveRequest = Roguelike.PlayerTurn.newBuilder().apply {
            player = playerId
            button = pressedButton
        }.build()
        stub.makeTurn(moveRequest)
    }

    fun getCurrentContext() = runBlocking {
        val packedContext = stub.updateMap(playerId).serializedMap
        levelLoader.unpackContext(packedContext)
    }
}