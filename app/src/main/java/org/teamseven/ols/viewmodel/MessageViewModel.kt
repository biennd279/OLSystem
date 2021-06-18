package org.teamseven.ols.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.network.MessageApiService
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.utils.SessionManager
import timber.log.Timber

class MessageViewModel(
    messageApiService: MessageApiService,
    appDatabase: AppDatabase,
    application: Application
) : ViewModel(){

    private val sessionManager = SessionManager(application)

    private var messageRepository = MessageRepository(
        appDatabase,
        messageApiService,
        sessionManager
    )


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
}