package org.teamseven.ols.network.websocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.teamseven.ols.network.websocket.WebSocketListener
import java.util.concurrent.TimeUnit

class WebSocketProvider(
    private val scope: CoroutineScope,
    wsUrl: String
) {
    private var webSocket: WebSocket
    private var textChannel = Channel<String>()

    init {
        val socketOkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(39, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .build()

        val request = Request.Builder()
            .url(wsUrl)
            .build()

        webSocket = socketOkHttpClient.newWebSocket(
            request,
            WebSocketListener(
                scope = scope,
                textChannel = textChannel
            )
        )

        socketOkHttpClient.dispatcher.executorService.shutdown()
    }

    fun asFlow(): Flow<String> = textChannel.consumeAsFlow().flowOn(Dispatchers.IO)

    fun close(code: Int, reason: String?) {
        scope.launch(Dispatchers.IO) {
            webSocket.close(code, reason)
            textChannel.close()
        }
    }

    fun send(text: String) {
        scope.launch(Dispatchers.IO) {
            webSocket.send(text)
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS: Int = 1000
    }
}