package org.teamseven.ols.network.socketio

import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.teamseven.ols.entities.Message
import org.teamseven.ols.utils.IOEvent
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty

class MessageSocketIOService(uri: String, token: String) {
    private lateinit var messageChannel: Channel<Result<Message?>>
    private lateinit var _socket: Socket
    val socket: Socket
        get() = _socket

    init {
        open(uri, token)
    }

    private fun open(uri: String, token: String) {
        messageChannel = Channel()

        val opts = IO.Options.builder()
            .setUpgrade(false)
            .setTransports(
                arrayOf(WebSocket.NAME)
            )
            .setAuth(
                mapOf("token" to token)
            )
            .build()

        _socket = IO.socket(uri, opts)

        _socket.on(Socket.EVENT_CONNECT_ERROR) {
            messageChannel.sendBlocking(Result.failure(Throwable("${it.toList()}")))
        }

        _socket.on(IOEvent.NEW_MESSAGE) {
            messageChannel.sendBlocking(
                Result.success(
                    it.first().toString().messageFromJson()
                )
            )
        }

        _socket.on(IOEvent.NEW_GROUP_CONVERSATION) {
            messageChannel.sendBlocking(
                Result.success(
                    it.first().toString().messageFromJson()
                )
            )
        }

        _socket.connect()
    }

    fun close() {
        _socket.close()
        messageChannel.close()
    }

    suspend fun sendMessage(message: Message) {
        withContext(Dispatchers.IO) {
            _socket.emit(IOEvent.NEW_MESSAGE, message)
        }
    }

    suspend fun newConversation(message: Message) {
        TODO()
    }

    fun asFlow() = this.messageChannel.consumeAsFlow().flowOn(Dispatchers.IO)

    private fun String.messageFromJson(): Message? {
        return Gson().fromJson(this, Message::class.java)
    }
}

