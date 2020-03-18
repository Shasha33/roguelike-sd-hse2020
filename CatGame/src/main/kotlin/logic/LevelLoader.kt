package logic

import com.google.gson.GsonBuilder
import data.*
import java.io.File

class LevelLoader {
    private val gson = GsonBuilder()
        .registerTypeAdapter(DoorUp::class.java, GameObjectSerializer())
        .registerTypeAdapter(DoorDown::class.java, GameObjectSerializer())
        .registerTypeAdapter(Wall::class.java, GameObjectSerializer())
        .registerTypeAdapter(Player::class.java, GameObjectSerializer())
        .registerTypeAdapter(Clew::class.java, GameObjectSerializer())
        .create()

    fun saveTo(context: Context, path: String) {
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
}