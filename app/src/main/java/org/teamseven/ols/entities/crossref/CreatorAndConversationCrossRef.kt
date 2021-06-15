package org.teamseven.ols.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.User

@Entity(
    primaryKeys = ["user_id", "conversation_id"]
)
data class CreatorAndConversationCrossRef(
    @ColumnInfo(name = "user_id")
    val creatorId: Int,

    @ColumnInfo(name = "conversation_id")
    val conversationId: Int,
) {
    constructor(creator: User, conversation: Conversation) : this(
            creatorId = creator.id,
            conversationId = conversation.id
    )
}
