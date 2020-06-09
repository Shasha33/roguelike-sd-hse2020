package com.sd.roguelike.logic

import com.sd.roguelike.data.*
import com.sd.roguelike.event.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EventsTest : StringSpec({
    "pick one clew test" {
        val context = Context(10, 10)
        val point = Point(0, 0)
        val player = Player()
        context.addObject(Clew(), point)
        context.addObject(player, point)
        val event = PickClewEvent()

        val result = event.apply(context)

        result.exitCode shouldBe ExitCode.CONTINUE
        player.cnt shouldBe 1
    }

    "pick several clews test" {
        val context = Context(10, 10)
        val point = Point(0, 0)
        val player = Player()
        context.addObject(Clew(), point)
        context.addObject(player, point)
        val event = PickClewEvent()

        val result1 = event.apply(context)

        result1.exitCode shouldBe ExitCode.CONTINUE
        player.cnt shouldBe 1

        context.addObject(Clew(), point)
        val result2 = event.apply(context)

        result2.exitCode shouldBe ExitCode.CONTINUE
        player.cnt shouldBe 2
    }

    "open door up test" {
        val context = Context(10, 10)
        context.addObject(
            Player(),
            Point(0, 0)
        )
        context.addObject(
            DoorUp(),
            Point(0, 0)
        )

        val result = NewLevelEvent().apply(context)
        result.exitCode shouldBe ExitCode.GO_UP
    }

    "open door down test" {
        val context = Context(10, 10)
        context.addObject(
            Player(),
            Point(0, 0)
        )
        context.addObject(
            DoorDown(),
            Point(0, 0)
        )

        val result = NewLevelEvent().apply(context)
        result.exitCode shouldBe ExitCode.GO_DOWN
    }

    "events in event bus test" {
        var cnt1 = 0
        var cnt2 = 0
        val addEvent = object : Event {
            override fun apply(context: Context): EventResult {
                cnt1++
                return EventResult(ExitCode.CONTINUE)
            }
        }
        val decEvent = object : Event {
            override fun apply(context: Context): EventResult {
                cnt2--
                return EventResult(ExitCode.EXIT)
            }
        }

        val eventBus = EventBus()
        eventBus.addEvent(addEvent)
        eventBus.addEvent(decEvent)

        eventBus.callAll(
            Context(
                10,
                10
            )
        ).exitCode shouldBe ExitCode.EXIT
        cnt1 shouldBe 1
        cnt2 shouldBe -1
    }

    "interrupted event queue test" {
        var cnt1 = 0
        var cnt2 = 0
        val addEvent = object : Event {
            override fun apply(context: Context): EventResult {
                cnt1++
                return EventResult(ExitCode.CONTINUE)
            }
        }
        val decEvent = object : Event {
            override fun apply(context: Context): EventResult {
                cnt2--
                return EventResult(ExitCode.EXIT)
            }
        }

        val eventBus = EventBus()
        eventBus.addEvent(decEvent)
        eventBus.addEvent(addEvent)

        eventBus.callAll(
            Context(
                10,
                10
            )
        ).exitCode shouldBe ExitCode.EXIT
        cnt1 shouldBe 0
        cnt2 shouldBe -1
    }

    "copy of events in event bus" {
        var cnt = 0
        val addEvent = object : Event {
            override fun apply(context: Context): EventResult {
                cnt++
                return EventResult(ExitCode.CONTINUE)
            }
        }

        val eventBus = EventBus()
        eventBus.addEvent(addEvent)
        eventBus.addEvent(addEvent)

        eventBus.callAll(
            Context(
                10,
                10
            )
        ).exitCode shouldBe ExitCode.CONTINUE
        cnt shouldBe 2
    }
})