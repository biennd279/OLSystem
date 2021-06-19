package org.teamseven.ols.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.db.MessageWithSender
import org.teamseven.ols.entities.requests.NewMessageRequest
import org.teamseven.ols.network.MessageApiService
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import timber.log.Timber

class MessageViewModel(
    messageApiService: MessageApiService,
    private val appDatabase: AppDatabase,
    application: Application
) : ViewModel(){

    private val sessionManager = SessionManager(application)

    private var messageRepository = MessageRepository(
        appDatabase,
        messageApiService,
        sessionManager
    )

    fun onUpdateToken() {
        messageRepository.onUpdateToken()
    }


    fun conversations(classroomId: Int) =
        if (classroomId != -1) {
            messageRepository.getAllConversationsInClassroom(classroomId)
                .flowOn(Dispatchers.IO)
                .catch { Timber.i(it.localizedMessage) }
                .asLiveData(viewModelScope.coroutineContext)
        } else {
            messageRepository.getAllConversation()
                .flowOn(Dispatchers.IO)
                .catch { Timber.i(it) }
                .asLiveData(viewModelScope.coroutineContext)
        }

    fun messages(conversationId: Int) = messageRepository.getConversationMessage(conversationId)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun members(conversationId: Int) = messageRepository.getConversationMembers(conversationId)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    @ExperimentalCoroutinesApi
    val newMessage = messageRepository.getNewMessage()
        .filter {
            it.status == Resource.Status.SUCCESS
        }
        .map {
            it.data
        }
        .filterNotNull()
        .map {
            val sender = appDatabase.userDao().findById(it.senderId).first()
            return@map Resource.success(
                MessageWithSender(
                    message = it,
                    sender = sender
                )
            )
        }
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    @ExperimentalCoroutinesApi
    fun newMessageToConversation(conversationId: Int) = messageRepository.getNewMessage()
        .filter {
            it.status == Resource.Status.SUCCESS
        }
        .map {
            it.data
        }
        .filterNotNull()
        .filter {
            it.conversationId == conversationId.toLong()
        }
        .map {
            val sender = appDatabase.userDao().findById(it.senderId).first()
            return@map Resource.success(
                MessageWithSender(
                    message = it,
                    sender = sender
                )
            )
        }
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun sendMessage(
        userId: Int,
        message: String,
        conversationId: Int
    ) = messageRepository.sendMessage(NewMessageRequest(
        senderId =  userId,
        message = message,
        messageText = message,
        conversationId = conversationId.toLong()
    ))

    fun conversation(conversationId: Int) = appDatabase.conversationDao().getConversation(conversationId)

    val stateListener = messageRepository.getSocketIoLifecycleObserver()
}