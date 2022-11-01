package com.sonnt.fpmerchant.network.stomp

import android.annotation.SuppressLint
import com.sonnt.fpmerchant.data.local.AuthDataSource
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.message.LoggedInEvent
import com.sonnt.fpmerchant.message.WSConnectedEvent
import com.sonnt.fpmerchant.network.Endpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage

class StompMessageHub {
    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Endpoint.WS_BASE_URL)
    private val gson = AppModule.provideGson()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val subscriberFlows = mutableMapOf<String, SharedFlow<*>>()

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onLoggedIn(event: LoggedInEvent) {
        if (event.isSessionExpired) {
            reconnect()
        } else {
            connectWS()
        }
    }

    private fun connectWS() {
        try {
            stompClient.lifecycle().asFlow()
                .filter { it.type == LifecycleEvent.Type.OPENED }
                .take(1)
                .onEach {
                    EventBus.getDefault().post(WSConnectedEvent())
                }
                .launchIn(coroutineScope)
            stompClient.connect(getConnectionHeaders())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getConnectionHeaders() = listOf(StompHeader("Authorization", "Bearer ${AuthDataSource.authToken}"))

    @SuppressLint("CheckResult")
    fun <T> subscribeTo(destination: String, clazz: Class<T>): SharedFlow<T>? {
        if (subscriberFlows[destination] == null) {
            subscriberFlows[destination] = stompClient.topic(destination).asFlow().map { topicMessage ->
                val jsonStr = topicMessage.payload

                val wsMessage = gson.fromJson(jsonStr, WSMessage::class.java)

                if (clazz == WSMessage::class.java) {
                    return@map wsMessage
                }

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

    @SuppressLint("CheckResult")
    fun reconnect() {
//        stompClient.disconnectCompletable()
//            .subscribe {
//                connectWS()
//            }
    }

}

