package com.sonnt.fpmerchant.network.stomp

import android.annotation.SuppressLint
import com.sonnt.fpmerchant.data.local.AuthDataSource
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.network.Endpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage

class StompMessageHub {
    private lateinit var stompClient: StompClient
    private val gson = AppModule.provideGson()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val subscriberFlows = mutableMapOf<String, SharedFlow<*>>()

    init {
        connectWS()
    }

    private fun connectWS(onConnected: () -> Unit = {}) {
        try {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Endpoint.WS_BASE_URL)
            stompClient.connect(listOf(StompHeader("Authorization", "Bearer ${AuthDataSource.authToken}")))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("CheckResult")
    fun <T> subscribeTo(destination: String, clazz: Class<T>): SharedFlow<T>? {

//        if (!stompClient.isConnected) {
//            return null
//        }

        if (subscriberFlows[destination] == null) {
            subscriberFlows[destination] = stompClient.topic(destination).asFlow().map { topicMessage ->
                val jsonStr = topicMessage.payload

                val wsMessage = gson.fromJson(jsonStr, WSMessage::class.java)

                if (clazz == String::class.java) {
                    return@map wsMessage.body
                }

                gson.fromJson(wsMessage.body, clazz)
            }.conflate().shareIn(coroutineScope, SharingStarted.Eagerly, 0)
        }

        return subscriberFlows[destination] as? SharedFlow<T>?
    }

    fun <T> sendJson(data: T, destination: String): Flow<Any> {
        val headers = listOf(
            StompHeader(StompHeader.CONTENT_TYPE, "application/json;charset=UTF-8"),
            StompHeader(StompHeader.DESTINATION, destination)
        )

        val payload = gson.toJson(data)

        val message = StompMessage(StompCommand.SEND, headers, payload)
        return stompClient.send(message).toFlowable<Any>().asFlow()
    }
}

