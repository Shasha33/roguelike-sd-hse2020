package data

import kotlinx.serialization.Serializable

@Serializable
data class Stats(var hp: Int, val attack: Int, val armor: Int) : Comparable<Stats> {
    override fun compareTo(other: Stats): Int {
        if (attack < other.attack) {
            return -1
        }
        if (attack == other.attack) {
            if (armor < other.armor) {
                return -1
            }
            return 0
        }
        return 1
    }
}