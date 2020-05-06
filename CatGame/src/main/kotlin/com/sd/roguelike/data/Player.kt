package com.sd.roguelike.data

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class Player : Unit {
    var cnt = 0

    fun addClew() {
        cnt++
    }
}

class DoorUp : Furniture
class DoorDown : Furniture
class Wall : Furniture

interface GameObject

interface Unit : GameObject
interface Pickable : GameObject
interface Furniture : GameObject

class Clew : Pickable

class GameObjectSerializer : JsonSerializer<GameObject> {
    override fun serialize(src: GameObject?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jo = JsonObject()
        jo.addProperty("className", src?.javaClass?.name)
        return jo
    }
}