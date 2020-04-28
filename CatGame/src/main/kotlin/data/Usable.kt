package data

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
abstract class Usable(val stats: Stats) : Pickable
@Serializable
class Hat(val bonus: Int): Usable(Stats(0, 0, bonus)) {
    companion object {
        val emptyHat = Hat(0)
    }
}
@Serializable
class Sword(val bonus: Int): Usable(Stats(0, bonus, 0)) {
    companion object {
        val emptySword = Sword(0)
    }
}

class HatBuilder(random: Random? = null) {
    private val random = random ?: Random(Random.nextInt())
    private var hatBonus = 0

    fun addBonus(bonus: Int): HatBuilder {
        hatBonus += bonus
        return this
    }

    fun build(): Hat {
        if (hatBonus == 0) {
            return Hat.emptyHat
        }
        return Hat(hatBonus)
    }

    fun withRandomBonus(): HatBuilder {
        hatBonus += random.nextInt() % 50
        return this
    }
}

class SwordBuilder(random: Random? = null) {
    private val random = random ?: Random(Random.nextInt())
    private var swordBonus = 0

    fun addBonus(bonus: Int): SwordBuilder {
        swordBonus += bonus
        return this
    }

    fun build(): Sword {
        if (swordBonus == 0) {
            return Sword.emptySword
        }
        return Sword(swordBonus)
    }

    fun withRandomBonus(): SwordBuilder {
        swordBonus += random.nextInt() % 50
        return this
    }
}