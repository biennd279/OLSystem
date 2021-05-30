package org.teamseven.ols.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = "messages"
)
data class Message(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "message_id")
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    @ColumnInfo(name = "sender_id")
    val senderId: Int,

    val message: String?,

    @SerializedName("message_text")
    @ColumnInfo(name = "message_text")
    val messageText: String,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    val createdAt: Date? = null,

    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null,

    @SerializedName("conversation_id")
    @ColumnInfo(name = "conversation_id")
    val conversationId: Long,

    @SerializedName("can_reply")
    @ColumnInfo(name = "can_reply")
    val canReply: Boolean = true,

    val attachment: String? = null
) {
    val isUpdated: Boolean
        get() = updatedAt == null || updatedAt != createdAt
}