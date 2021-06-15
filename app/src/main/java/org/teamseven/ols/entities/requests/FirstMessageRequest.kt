package org.teamseven.ols.entities.requests

import com.google.gson.annotations.SerializedName

data class FirstMessageRequest(
    @SerializedName("sender_id")
    val senderId: Int,

    val message: String?,

    @SerializedName("message_text")
    val messageText: String,

    @SerializedName("can_reply")
    val canReply: Boolean = true,

    val attachment: String? = null
)
