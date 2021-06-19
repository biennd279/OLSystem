package org.teamseven.ols.entities.responses

import com.google.gson.annotations.SerializedName
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message

data class NewConversationResponse(
    @SerializedName("conversation")
    val conversation: Conversation,

    @SerializedName("first_message")
    val firstMessage: Message
)