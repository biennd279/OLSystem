package org.teamseven.ols.entities.db

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.SenderAndMessageCrossRef

data class MessageWithSender(
    @Embedded
    val message: Message,

    @Relation(
        parentColumn = "message_id",
        entityColumn = "user_id",
        associateBy = Junction(SenderAndMessageCrossRef::class)
    )
    val sender: User
)
