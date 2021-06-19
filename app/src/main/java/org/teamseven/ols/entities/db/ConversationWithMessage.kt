package org.teamseven.ols.entities.db

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.crossref.ConversationAndMessageCrossRef

data class ConversationWithMessage(
    @Embedded
    val conversation: Conversation,

    @Relation(
        parentColumn = "conversation_id",
        entityColumn = "message_id",
        entity = Message::class
    )
    val messages: List<MessageWithSender>
)