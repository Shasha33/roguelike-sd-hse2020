package data

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class Player : Unit {
    override val stats = Stats(100, 10, 0 )
    private val inventory = Inventory()

    fun addClew() {
        stats.hp++
    }

    fun pickHat(hat: Hat) {
        inventory.addHat(hat)
    }
    fun pickSword(sword: Sword) {
        inventory.addSword(sword)
    }

    override val damageValue: Int
        get() {
            return if (isAlive()) stats.attack + inventory.attackBonus else 0
        }

    override fun damage(value: Int) {
        stats.hp -= value - inventory.armorBonus - stats.armor
    }
}

class DoorUp : Furniture
class DoorDown : Furniture
class Wall : Furniture

interface GameObject

interface  Unit : GameObject {
    val stats : Stats

    val damageValue: Int
        get() {
        return if (isAlive()) stats.attack else 0
    }

    fun damage(value: Int) {
        stats.hp -= value
    }

    fun isAlive(): Boolean {
        return stats.hp > 0
    }
}

interface Pickable : GameObject
interface Furniture : GameObject

class Clew : Pickable

abstract class Usable(val stats: Stats) : Pickable
class Hat(bonus: Int): Usable(Stats(0, 0, bonus)) {
    companion object {
        val emptyHat = Hat(0)
    }
}
class Sword(bonus: Int): Usable(Stats(0, bonus, 0)) {
    companion object {
        val emptySword = Sword(0)
    }
}

class GameObjectSerializer : JsonSerializer<GameObject> {
    override fun serialize(src: GameObject?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jo = JsonObject()
        jo.addProperty("className", src?.javaClass?.name)
        return jo
    }
}