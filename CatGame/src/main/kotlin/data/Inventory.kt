package data

import kotlinx.serialization.Serializable

@Serializable
class Inventory {
    private var hat : Hat = Hat.emptyHat
    private var sword : Sword = Sword.emptySword

    val armorBonus : Int
        get() = hat.stats.armor + sword.stats.armor
    val attackBonus : Int
        get() = hat.stats.attack + sword.stats.attack

    fun addHat(newHat: Hat): Hat {
        if (hat.stats > newHat.stats) {
            val oldHat = hat
            hat = newHat
            return oldHat
        }
        return newHat
    }

    fun addSword(newSword: Sword): Sword {
        if (sword.stats > newSword.stats) {
            val oldSword = sword
            sword = newSword
            return oldSword
        }
        return newSword
    }

    fun dropHat() {
        hat = Hat.emptyHat
    }

    fun dropSword() {
        sword = Sword.emptySword
    }
}