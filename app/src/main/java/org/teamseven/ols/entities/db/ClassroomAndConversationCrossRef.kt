package org.teamseven.ols.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["classroom_id", "conversation_id"]
)
data class ClassroomAndConversationCrossRef(
    @ColumnInfo(name = "classroom_id")
    val classroomId: Int,

    @ColumnInfo(name = "conversation_id")
    val conversationId: Int
)
