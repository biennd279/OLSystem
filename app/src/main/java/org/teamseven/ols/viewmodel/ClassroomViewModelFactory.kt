package org.teamseven.ols.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.network.ClassroomService

class ClassroomViewModelFactory(
    private val classroomService: ClassroomService,
    private val appDatabase: AppDatabase,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassroomViewModel::class.java)) {
            return ClassroomViewModel(
                classroomService,
                appDatabase,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}