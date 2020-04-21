package views

import data.*

class LevelRender {

    fun drawContext(weight: Int, height: Int, ctx: Map<Point, List<GameObject>>): String {
        val builder = LevelBuilder(weight, height)
        for ((point, list) in ctx.entries) {
            if (!list.isEmpty()) {
                val sprite = loadSprite(list.first())
                builder.drawOn(point.x, point.y, sprite)
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
            is DoorUp -> "˄"
            is DoorDown -> "˅"
            else -> " "
        }

}

class LevelBuilder(val w: Int, val h: Int) {
    private val lines: List<MutableList<String>>

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
        }
    }
}