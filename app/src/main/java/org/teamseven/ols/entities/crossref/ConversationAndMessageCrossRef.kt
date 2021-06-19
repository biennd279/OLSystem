package org.teamseven.ols.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["conversation_id", "message_id"],
    tableName = "conversation_and_message_cross_ref"
)
data class ConversationAndMessageCrossRef(
    @ColumnInfo(name = "conversation_id")
    val conversationId: Long,

    @ColumnInfo(name = "message_id")
    val messageId: Int
)