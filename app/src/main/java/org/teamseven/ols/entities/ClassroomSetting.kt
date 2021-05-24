package org.teamseven.ols.entities

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ClassroomSetting(
    @ColumnInfo(name = "require_approval")
    @SerializedName("require_approval")
    var isRequiredApproval: Int,

    @ColumnInfo(name = "participant_messaging")
    @SerializedName("participant_messaging")
    var typeParticipantMessage: String,

    @ColumnInfo(name = "message_with_children")
    @SerializedName("message_with_children")
    var canMessageWithChildren: Int,
) {
    //TODO fix type participant message
    enum class TypeParticipantMessage {
        ON, OFF, ROLE_BASE;
    }
}