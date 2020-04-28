package data

class Inventory {
    private var hat : Hat = Hat.emptyHat()
    private var sword : Sword = Sword.emptySword()

    val armorBonus : Int
        get() = hat.stats.armor + sword.stats.armor
    val attackBonus : Int
        get() = hat.stats.attack + sword.stats.attack

    fun addHat(newHat: Hat): Boolean {
        if (hat.stats.armor > newHat.stats.armor) {
            hat = newHat
            return true
        }
        return false
    }

    fun addSword(newSword: Sword): Boolean {
        if (sword.stats.attack > sword.stats.attack) {
            sword = newSword
            return true
        }
        return false
    }

    fun dropHat() {
        hat = Hat.emptyHat()
    }

    fun dropSword() {
        sword = Sword.emptySword()
    }
}