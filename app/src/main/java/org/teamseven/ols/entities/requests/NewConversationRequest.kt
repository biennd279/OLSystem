package org.teamseven.ols.entities.requests

import com.google.gson.annotations.SerializedName
import org.teamseven.ols.entities.Message

data class NewConversationRequest(
    @SerializedName("sender_id")
    val senderId: Int,

    val message: FirstMessageRequest,

    @SerializedName("receiver_ids")
    val receiverIds: List<Int>,

    val attachment: String?,

    @SerializedName("classroom_id")
    val classroomId: Int
)
