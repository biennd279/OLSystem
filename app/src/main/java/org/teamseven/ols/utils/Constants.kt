package org.teamseven.ols.utils

import org.teamseven.ols.utils.Domain.DOMAIN

object Constants {

    const val BASE_URL = "${DOMAIN}:3000/api/"
    const val USER_URL = "user"
    const val LOGIN_URL = "auth/login"
    const val MESSAGE_URL = "message"
    const val CLASSROOM_URL = "classroom"
    const val TIME_EXPIRE = 86400L
    const val MESSAGE_NAMESPACE = "message"
}

object IOEvent {
    const val JOIN_CHANNEL = "JOIN_CHANNEL"
    const val NEW_MESSAGE = "NEW_MESSAGE"
    const val NEW_GROUP_CONVERSATION = "NEW_GROUP_CONVERSATION"
    const val WRONG_MESSAGE = "WRONG_MESSAGE"
    const val PROCESS_ERROR = "PROCESS_ERROR"
}