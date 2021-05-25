package org.teamseven.ols.network.websocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


class WebSocketListener(
    private val scope: CoroutineScope,
    private var textChannel: Channel<String>
): WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {}

    override fun onMessage(webSocket: WebSocket, text: String) {
        scope.launch(Dispatchers.IO) {
            textChannel.send(text)
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        this.onMessage(webSocket, bytes.toString())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        textChannel.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        textChannel.close(t)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS: Int = 1000
    }

}