package org.teamseven.ols.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import org.teamseven.ols.R
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.UserRepository
import java.util.*

class MessageViewModel(context: Context) : ViewModel(){

    private lateinit var authService: AuthService
    private lateinit var userService: UserService
    private lateinit var userDao: UserDao
    private lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository

    init {
        authService = AuthService.create(context)
        userService = UserService.create(context)!!
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = appDatabase.userDao()

        userRepository = UserRepository(
            userService,
            authService,
            userDao
        )
    }


    fun getListOfMessage() {
        val avatar = Arrays.asList(R.array.avatar)
        val username = Arrays.asList(R.array.username)
        val status = Arrays.asList(R.array.status)
        val time = Arrays.asList(R.array.time)

        /*
        val messageItems : List<MessageItem> = ListOf()

        for (i in username.indices) {
            messageItems.add(
                MessageItem(
                    username[i],
                    status[i],
                    avatar.getResourceId(i, 0),
                    time[i]
                )
            )
        }*/
    }
}