package data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Player(val id : Int = 0, @Transient private val dieCallback: () -> kotlin.Unit = {}) : Unit {
    constructor(id : Int = 0) : this(id, {})

    override val stats = Stats(100, 10, 0 )
    private val inventory = Inventory()

    fun addClew() {
        stats.hp++
    }

    fun pickHat(hat: Hat): Hat {
        return inventory.addHat(hat)
    }
    fun pickSword(sword: Sword): Sword {
        return inventory.addSword(sword)
    }

    override val damageValue: Int
        get() {
            return if (isAlive()) stats.attack + inventory.attackBonus else 0
        }

    val armorValue : Int
        get() = inventory.armorBonus + stats.armor

    //can be used for heal
    override fun damage(value: Int) {
        stats.hp -= value - inventory.armorBonus - stats.armor
    }

    fun onDie() {
        dieCallback()
    }
}
@Serializable
class DoorUp : Furniture
@Serializable
class DoorDown : Furniture
@Serializable
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

@Serializable
class Clew : Pickable