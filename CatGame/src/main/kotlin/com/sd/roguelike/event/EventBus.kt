package com.sd.roguelike.event

import com.sd.roguelike.data.Context

class EventBus {
    private val events = mutableListOf<Event>()

    fun addEvent(event: Event) {
        events.add(event)
    }

    fun removeEvent(event: Event) {
        events.remove(event)
    }

    fun addAll(events: List<Event>) {
        this.events.addAll(events)
    }

    fun callAll(context: Context): EventResult {
        for (event in events) {
            val result = event.apply(context)
            if (result.exitCode != ExitCode.CONTINUE) {
                return result
            }
        }
        return EventResult(ExitCode.CONTINUE)
    }
}