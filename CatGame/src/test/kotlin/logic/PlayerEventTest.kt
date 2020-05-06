package logic

import data.Context
import data.Player
import data.Point
import data.Wall
import event.ExitCode
import event.PlayerEvent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.concurrent.LinkedBlockingQueue

class PlayerEventTest : StringSpec({
    val queue = LinkedBlockingQueue<String>()
    val event = PlayerEvent(queue)

    "move player up test" {
        val context = Context(100, 100)
        val point = Point(50, 50)
        context.addObject(Player(), point)
        queue.add("UP")

        val result = event.apply(context)

        val newPoint = context.getPlayerPoint()
        newPoint shouldNotBe null
        result.exitCode shouldBe ExitCode.CONTINUE
        newPoint shouldBe point.plus(Point(0, -1))
    }

    "move player down test" {
        val context = Context(100, 100)
        val point = Point(50, 50)
        context.addObject(Player(), point)
        queue.add("DOWN")

        val result = event.apply(context)

        val newPoint = context.getPlayerPoint()
        newPoint shouldNotBe null
        result.exitCode shouldBe ExitCode.CONTINUE
        newPoint shouldBe point.plus(Point(0, 1))
    }

    "move player right test" {
        val context = Context(100, 100)
        val point = Point(50, 50)
        context.addObject(Player(), point)
        queue.add("RIGHT")

        val result = event.apply(context)

        val newPoint = context.getPlayerPoint()
        newPoint shouldNotBe null
        result.exitCode shouldBe ExitCode.CONTINUE
        newPoint shouldBe point.plus(Point(1, 0))
    }

    "move player left test" {
        val context = Context(100, 100)
        val point = Point(50, 50)
        context.addObject(Player(), point)
        queue.add("LEFT")

        val result = event.apply(context)

        val newPoint = context.getPlayerPoint()
        newPoint shouldNotBe null
        result.exitCode shouldBe ExitCode.CONTINUE
        newPoint shouldBe point.plus(Point(-1, 0))
    }

    "end game test" {
        val context = Context(100, 100)
        val point = Point(50, 50)
        context.addObject(Player(), point)
        queue.add("ItsTimeToStop")

        val result = event.apply(context)

        result.exitCode shouldBe ExitCode.EXIT
    }

    "try to move player to wall test" {
        val context = Context(100, 100)
        val point = Point(50, 50)
        context.addObject(Player(), point)
        context.addObject(Wall(), point.plus(Point(0, -1)))
        queue.add("UP")

        val result = event.apply(context)

        val newPoint = context.getPlayerPoint()
        newPoint shouldNotBe null
        result.exitCode shouldBe ExitCode.CONTINUE
        newPoint shouldBe point
    }

    "try to move player out of field test" {
        val context = Context(100, 100)
        val point = Point(0, 0)
        context.addObject(Player(), point)
        queue.add("UP")

        val result = event.apply(context)

        val newPoint = context.getPlayerPoint()
        newPoint shouldNotBe null
        result.exitCode shouldBe ExitCode.CONTINUE
        newPoint shouldBe point
    }
})