package data

import com.sun.xml.internal.bind.v2.schemagen.episode.Klass
import kotlin.reflect.KClass

class Context {
    private val objects = mutableMapOf<Point, MutableList<GameObject>>()

    fun moveObject(type: KClass<out GameObject>, from: Point, to: Point) {
        val currentObject = (objects[from] ?: return).singleOrNull { type.isInstance(it) } ?: return
        objects[from]?.remove(currentObject)
        objects.getOrPut(to, { mutableListOf()}).add(currentObject)
    }

    fun getObjectsAt(x: Int, y: Int): List<GameObject>? {
        return objects[Point(x, y)]
    }

    fun getPlayerPoint(): Point {
        objects.map { (p: Point, g: List<GameObject>) ->
            if (g.)
        }
    }
}

data class Point(private val x: Int, private val y: Int)