package logic

import data.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe

class ContextTest : StringSpec({
    "move object test" {
        val context = Context(10, 10)
        val point = Point(1, 1)
        val newPoint = point.plus(Point(1 ,0))
        context.addObject(Clew(), point)

        context.moveObject(Clew::class, point, newPoint)
        val map = context.getMap()
        map.filterValues { it.isNotEmpty() } shouldHaveSize 1
        map[point]?.shouldHaveSize(0)
        map[newPoint]?.shouldHaveSize(1)
    }

    "move nonexistent object test" {
        val context = Context(10, 10)
        val truePoint = Point(0, 0)
        val point = Point(1, 1)
        val newPoint = point.plus(Point(1 ,0))
        val clew = Clew()
        context.addObject(clew, truePoint)

        context.moveObject(Clew::class, point, newPoint)
        val map = context.getMap()
        map.filterValues { it.isNotEmpty() } shouldHaveSize 1
        map[truePoint] shouldContainExactly listOf(clew)
        map[point]?.shouldHaveSize(0)
        map[newPoint]?.shouldHaveSize(0)
    }

    "remove object test" {
        val context = Context(10, 10)
        val point = Point(1, 1)
        context.addObject(Clew(), point)

        context.removeObject(Clew::class, point)
        val map = context.getMap()
        map.filterValues { it.isNotEmpty() } shouldHaveSize 0
        map[point]?.shouldHaveSize(0)
    }

    "remove nonexistent object test" {
        val context = Context(10, 10)
        val point = Point(1, 1)
        val clew = Clew()
        context.addObject(clew, point)

        context.removeObject(Clew::class, point.plus(Point(1, 1)))
        val map = context.getMap()
        map.filterValues { it.isNotEmpty() } shouldHaveSize 1
        map[point] shouldContainExactly listOf(clew)
    }

    "is wall point positive test" {
        val context = Context(10, 10)
        val point = Point(1,1)
        context.addObject(Wall(), point)

        context.isWall(point) shouldBe true
    }

    "is wall point negative test" {
        val context = Context(10, 10)
        val point = Point(1,1)

        context.isWall(point) shouldBe false
    }

    "is point in field test" {
        val h = 10
        val w = 10
        val context = Context(h, w)

        context.inField(Point(0, 0)) shouldBe true
        context.inField(Point(h - 1, w - 1)) shouldBe true
        context.inField(Point(h, 0)) shouldBe false
        context.inField(Point(0, w)) shouldBe false
        context.inField(Point(-1, 0)) shouldBe false
    }

    "add object test" {
        val context = Context(10, 10)
        val point = Point(1, 1)
        val clew = Clew()
        context.addObject(clew, point)

        val map = context.getMap()
        map.filterValues { it.isNotEmpty() } shouldHaveSize 1
        map[point] shouldContainExactly listOf(clew)
    }

    "get player test" {
        val context = Context(10, 10)
        val point = Point(1, 1)
        val player = Player()
        context.addObject(player, point)

        context.getPlayer() shouldBe player
        context.getPlayerPoint() shouldBe point
    }

    "two players on map test" {
        val context = Context(10, 10)
        val point = Point(1, 1)
        val player = Player()
        context.addObject(player, point)
        context.addObject(player, point)

        context.getPlayer() shouldBe null
    }

    "get player on empty map test" {
        val context = Context(10, 10)

        context.getPlayer() shouldBe null
    }
})