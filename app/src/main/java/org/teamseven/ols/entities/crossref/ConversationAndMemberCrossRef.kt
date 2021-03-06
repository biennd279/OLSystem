package org.teamseven.ols.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["conversation_id", "user_id"],
    tableName = "conversation_and_member_cross_ref"
)
data class ConversationAndMemberCrossRef(
    @ColumnInfo(name = "conversation_id")
    val conversationId: Int,

    @ColumnInfo(name = "user_id")
    val memberId: Int
)
