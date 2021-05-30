package org.teamseven.ols.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["user_id", "message_id"]
)
data class SenderAndMessageCrossRef(
    @ColumnInfo(name = "user_id")
    val senderId: Int,

    @ColumnInfo(name = "message_id")
    val messageId: Int
)
