package org.teamseven.ols.entities

import com.google.gson.annotations.SerializedName

enum class TypeParticipantMessage {
    //TODO add enum type message
}

data class ClassroomSetting(
    @SerializedName("require_approval")
    var isRequiredApproval: Int,
    @SerializedName("participant_messaging")
    var typeParticipantMessage: String,
    @SerializedName("message_with_children")
    var canMessageWithChildren: Int,
)