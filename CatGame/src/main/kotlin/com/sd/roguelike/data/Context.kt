package com.sd.roguelike.data

import kotlin.properties.Delegates
import kotlin.reflect.KClass

class Context(val height: Int, val width: Int) {
    private val objects = mutableMapOf<Point, MutableList<GameObject>>()
    private var runnableReaction: (Map<Point, List<GameObject>>) -> Any = {}
    private var stepsCount: Int by Delegates.observable(0) { _, _, _ ->
        runnableReaction.invoke(getMap())
    }

    fun getMap(): Map<Point, List<GameObject>> {
        return HashMap(objects)
    }

    fun addReaction(reaction: (Map<Point, List<GameObject>>) -> Any) {
        runnableReaction = reaction
        stepsCount++
    }

    fun moveObject(type: KClass<out GameObject>, from: Point, to: Point) {
        val currentObject = (objects[from] ?: return).singleOrNull { type.isInstance(it) } ?: return
        objects[from]?.remove(currentObject)
        objects.getOrPut(to, { mutableListOf() }).add(currentObject)
        stepsCount++
    }

    fun addObject(gameObject: GameObject, p: Point) {
        objects.getOrPut(p) { mutableListOf() }.add(gameObject)
        stepsCount++
    }

    fun removeObject(type: KClass<out GameObject>, from: Point) {
        objects[from] ?: return
        objects[from] = objects[from]!!.filter { !type.isInstance(it) }.toMutableList()
        stepsCount++
    }

    private fun getObjectsAt(p: Point): List<GameObject>? {
        return objects[p]
    }

    fun getPlayerPoint(): Point? {
        return objects.mapNotNull { (p: Point, g: List<GameObject>) ->
            if (g.any { it is Player }) {
                p
            } else {
                null
            }
        }.singleOrNull()
    }

    private fun getTypeObjectAt(type: KClass<out GameObject>, p: Point): GameObject? {
        return getObjectsAt(p)?.singleOrNull { type.isInstance(it) }
    }

    fun getPlayer(): Player? {
        val playerPoint = getPlayerPoint() ?: return null
        return getTypeObjectAt(Player::class, playerPoint) as? Player
    }

    fun containsClass(type: KClass<out GameObject>, p: Point): Boolean {
        return objects.getOrDefault(p, emptyList<GameObject>()).any { type.isInstance(it) }
    }

    fun isWall(p: Point): Boolean {
        return containsClass(Wall::class, p)
    }

    fun inField(p: Point): Boolean {
        return (0 <= p.x) && (0 <= p.y) && (width > p.x) && (height > p.y)
    }
}

data class Point(val x: Int, val y: Int)