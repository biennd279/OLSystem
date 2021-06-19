package org.teamseven.ols.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["user_id", "message_id"],
    tableName = "sender_and_message_cross_ref"
)
data class SenderAndMessageCrossRef(
    @ColumnInfo(name = "user_id")
    val senderId: Int,

    @ColumnInfo(name = "message_id")
    val messageId: Int
)
