package views

import data.*

class LevelRender {

    fun drawContext(width: Int, height: Int, ctx: Map<Point, List<GameObject>>): String {
        val builder = LevelBuilder(width, height)
        for ((point, list) in ctx.entries) {
            if (!list.isEmpty()) {
                val obj = list.first()
                val sprite = loadSprite(obj)
                builder.drawOn(point.x, point.y, sprite)
                if (obj is Player) {
                    builder.hp = obj.stats.hp
                    builder.armor = obj.armorValue
                    builder.pow = obj.damageValue
                }
            }
        }
        return builder.toString()
    }

    private fun loadSprite(obj: GameObject): String =
        when (obj) {
            is Wall -> "█"
//            is Player -> "\uD83D\uDC31"
//            is DoorUp -> "\uD83D\uDEAA"
//            is DoorDown -> "\uD83D\uDD73"
            is Player -> "☺"
            is DoorUp -> "∆"
            is DoorDown -> "ӿ"
            is Enemy -> "©"
            is Clew -> "@"
            is Hat -> "H"
            is Sword -> "S"
            else -> "X"
        }

}

class LevelBuilder(val w: Int, val h: Int) {
    private val lines: List<MutableList<String>>
    var hp = -1
    var armor = -1
    var pow = -1

    init {
        val iniList = generateSequence { " " }.take(w).toList()
        lines = generateSequence { iniList.toMutableList() }.take(h).toList()
    }

    fun drawOn(x: Int, y: Int, sprite: String): Boolean {
        if (x < 0 || x >= w || y < 0 || y >= h) {
            // ignoring
            return false
        }
        lines[y][x] = sprite
            return true
    }

    override fun toString(): String {
        return buildString {
            for (ln in lines) {
                for (sprite in ln) {
                    append(sprite)
                }
                append("\n")
            }
            if (hp != -1) {
                append(" ".repeat(w / 3) + "HP: $hp; Armor: $armor; AP: $pow; ")
            } else {
                append(" ".repeat(w / 3) + "GAME OVER")
            }
        }
    }
}