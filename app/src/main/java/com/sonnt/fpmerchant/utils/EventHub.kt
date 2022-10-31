package com.sonnt.fpmerchant.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.RawValue

enum class EventCode(val rawValue: String) {
    sessionExpired("sessionExpired"),
    loggedIn("loggedIn"),
    connectedWS("connectedWS")
}

data class Event(
    val eventCode: String,
    val params: Map<String, Any> = mapOf()
)

object EventHub {

    private val flows: MutableMap<String, MutableSharedFlow<Event>> = mutableMapOf()

    fun post(eventCode: String, params: Map<String, Any> = mapOf()) {
        val eventFlow = getOrCreateEventFlow(eventCode)
        eventFlow.tryEmit(Event(eventCode, params))
    }

    fun subscribe(eventCode: String, scope: CoroutineScope, onEvent: (Map<String, Any>) -> Unit) {
        val eventFlow = getOrCreateEventFlow(eventCode)

        eventFlow.onEach {
            Log.i("eventhub", it.eventCode)
            onEvent(it.params)
        }.launchIn(scope)
    }

    private fun getOrCreateEventFlow(eventCode: String): MutableSharedFlow<Event> {
        var eventFlow = flows[eventCode]
        if (eventFlow == null) {
            eventFlow = MutableSharedFlow(extraBufferCapacity = 1)
            flows[eventCode] = eventFlow
        }

        return eventFlow
    }
}