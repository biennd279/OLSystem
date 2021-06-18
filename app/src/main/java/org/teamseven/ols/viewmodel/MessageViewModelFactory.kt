package org.teamseven.ols.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.network.MessageApiService

class MessageViewModelFactory(
    private val messageApiService: MessageApiService,
    private val appDatabase: AppDatabase,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return MessageViewModel(
                messageApiService,
                appDatabase,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}