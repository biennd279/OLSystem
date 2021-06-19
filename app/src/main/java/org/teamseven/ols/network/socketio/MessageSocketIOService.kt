package org.teamseven.ols.network.socketio

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.requests.NewConversationRequest
import org.teamseven.ols.entities.requests.NewMessageRequest
import org.teamseven.ols.entities.responses.NewConversationResponse
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.IOEvent
import org.teamseven.ols.utils.Resource

class MessageSocketIOService(uri: String, token: String) {

    private var opts: IO.Options = IO.Options.builder()
        .setUpgrade(false)
        .setTransports(
            arrayOf(WebSocket.NAME)
        )
        .setAuth(
            mutableMapOf("token" to token)
        )
        .setTimeout(Constants.TIME_EXPIRE)
        .build()

    private val _socket: Socket = IO.socket(uri, opts)
    val socket: Socket
        get() = _socket


    fun updateToken(token: String) {
        _socket.close()
        opts.auth["token"] = token
        _socket.open()
    }

    fun open() {
        _socket.connect()
    }

    @ExperimentalCoroutinesApi
    fun messages() = callbackFlow {
        val onMessage = Emitter.Listener {
            val message = it.first().toString().messageFromJson()
            offer(message)
        }

        _socket.on(IOEvent.NEW_MESSAGE, onMessage)

        awaitClose { _socket.off(IOEvent.NEW_MESSAGE, onMessage) }
    }

    @ExperimentalCoroutinesApi
    fun newConversations() = callbackFlow {
        val onConversation = Emitter.Listener {
            val newConversation = it.first().toString().newConversationFromJson()
            offer(newConversation)
        }

        _socket.on(IOEvent.NEW_GROUP_CONVERSATION, onConversation)

        awaitClose { _socket.off(IOEvent.NEW_GROUP_CONVERSATION, onConversation) }
    }

    fun sendMessage(newMessageRequest: NewMessageRequest) {
        _socket.emit(IOEvent.NEW_MESSAGE, Gson().toJson(newMessageRequest))
    }

    fun newConversation(newConversationRequest: NewConversationRequest) {
        _socket.emit(IOEvent.NEW_GROUP_CONVERSATION,  Gson().toJson(newConversationRequest))
    }

    fun close() {
        _socket.close()
    }

    private fun String.messageFromJson(): Message? {
        return Gson().fromJson(this, Message::class.java)
    }

    private fun String.newConversationFromJson(): NewConversationResponse? {
        return Gson().fromJson(this, NewConversationResponse::class.java)
    }
}

