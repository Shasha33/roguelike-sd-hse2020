package logic

import data.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.reflect.KClass

class LevelGeneratingTest : StringSpec({
    val seed = 42
    val levelProducer = LevelProducer()
    val context = levelProducer.create(seed)

    fun Context.findGameObject(type: KClass<out GameObject>): List<Point> {
        val map = getMap()
        return map.filter { (_: Point, list: List<GameObject>) -> list.any { type.isInstance(it) } }
            .map { it.component1() }
    }

    "player presents" {
        context.getPlayer() shouldNotBe null
        context.getPlayerPoint() shouldNotBe null
    }

    "door up presents" {
        context.findGameObject(DoorUp::class) shouldHaveSize 1
    }

    "door down presents" {
        context.findGameObject(DoorDown::class) shouldHaveSize 1
    }

    "at least one clew presents" {
        context.findGameObject(Clew::class) shouldHaveAtLeastSize 1
    }

    "no more than one object at a point" {
        context.getMap().forEach { (_, list: List<GameObject>) ->
            list shouldHaveSize 1
        }
    }

    fun dfs(index: Int, used: Array<Boolean>, points: List<Point>, context: Context) {
        used[index] = true
        val point = points[index]
        val deltas = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))
        val neighbours = deltas.map{ it.plus(point) }.map { points.indexOf(it) }.filter { it >= 0 }
        for (u in neighbours) {
            if (!used[u]) {
                dfs(u, used, points, context)
            }
        }
    }

    "all not wall points connected" {

        val notWallPoints = mutableListOf<Point>()
        for (x in (0 until context.height)) {
            for (y in (0 until context.width)) {
                val point = Point(y, x)
                if (!context.isWall(point)) {
                    notWallPoints.add(point)
                }
            }
        }
        val n = notWallPoints.size
        val used = Array(n) { false }
        dfs(0, used, notWallPoints, context)
        used.forEach { it shouldBe true }
    }
})