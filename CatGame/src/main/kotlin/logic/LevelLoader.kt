package logic

import com.google.gson.GsonBuilder
import data.*
import java.io.File

class LevelLoader {
    private val defaultSavePath = "savedData_level"
    private var lastSavePath = defaultSavePath
    private val gson = GsonBuilder()
        .registerTypeAdapter(DoorUp::class.java, GameObjectSerializer())
        .registerTypeAdapter(DoorDown::class.java, GameObjectSerializer())
        .registerTypeAdapter(Wall::class.java, GameObjectSerializer())
        .registerTypeAdapter(Player::class.java, GameObjectSerializer())
        .registerTypeAdapter(Clew::class.java, GameObjectSerializer())
        .create()

    init {
        val file = File(defaultSavePath)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun saveTo(context: Context, path: String) {
        if (path != lastSavePath) {
            lastSavePath = path
        }
        val jsonText = gson.toJson(context)
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(jsonText)
    }

    fun loadFrom(path: String): Context {
        val text = File(path).readText()
        return gson.fromJson(text, Context::class.java)
    }

    fun nextLevel(context: Context) {
        saveTo(context, lastSavePath)
    }

    fun getSavedLevel(): Context {
        return loadFrom(lastSavePath)
    }
}