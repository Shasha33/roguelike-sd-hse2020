package logic


import data.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import java.io.File

class LevelLoader {
    private val defaultSavePath = "savedData_level.json"
    private var lastSavePath = defaultSavePath
    private val gameObjectModule = SerializersModule {
        polymorphic(GameObject::class) {
            Wall::class with Wall.serializer()
            DoorUp::class with DoorUp.serializer()
            DoorDown::class with DoorDown.serializer()
            Player::class with Player.serializer()
            Clew::class with Clew.serializer()
            Hat::class with Hat.serializer()
            Sword::class with Sword.serializer()
            Enemy::class with Enemy.serializer()
        }
    }
    private val strategyModule = SerializersModule {
        polymorphic(EnemyStrategy::class) {
            AggressiveStrategy::class with AggressiveStrategy.serializer()
            StrategyDecorator::class with StrategyDecorator.serializer()
            ContusedStrategy::class with ContusedStrategy.serializer()
            PassiveStrategy::class with PassiveStrategy.serializer()
            PassiveAggressiveStrategy::class with PassiveAggressiveStrategy.serializer()
        }
    }
    private val json = Json(context = gameObjectModule + strategyModule)

    fun saveTo(context: Context, path: String = defaultSavePath) {
        lastSavePath = path
        val jsonText = packContext(context)
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(jsonText)
    }

    fun loadFrom(path: String = defaultSavePath): Context? {
        val file = File(path)
        if (!file.exists() || !file.canRead()) {
            return null
        }
        val text = file.readText()
        return unpackContext(text)
    }

    // for server map update sending
    fun packContext(context: Context): String {
        return json.stringify(Context.serializer(), context)
    }

    // for client map receiving
    fun unpackContext(text: String): Context {
        return json.parse(Context.serializer(), text)
    }

    fun nextLevel(context: Context) {
        saveTo(context, lastSavePath)
    }

    fun getSavedLevel(): Context? {
        return loadFrom(lastSavePath)
    }

    fun dropLastSave() {
        File(lastSavePath).delete()
    }
}