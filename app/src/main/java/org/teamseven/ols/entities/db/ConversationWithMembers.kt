package org.teamseven.ols.entities.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.ConversationAndMemberCrossRef
import org.teamseven.ols.entities.crossref.ConversationAndMessageCrossRef

data class ConversationWithMembers(
    @Embedded
    val conversation: Conversation,

    @Relation(
        parentColumn = "conversation_id",
        entityColumn = "user_id",
        associateBy = Junction(ConversationAndMemberCrossRef::class)
    )
    val members: List<User>,
)