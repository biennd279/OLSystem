package org.teamseven.ols.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.crossref.ConversationAndMessageCrossRef
import org.teamseven.ols.entities.crossref.SenderAndMessageCrossRef

@Dao
interface MessageDao {

    @Query("select * from messages")
    fun getAllMessage(): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg message: Message)

    @Update
    suspend fun update(vararg message: Message)

    @Delete
    suspend fun delete(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversationMessage(
        vararg conversationAndMessageCrossRef: ConversationAndMessageCrossRef
    )

    @Delete
    suspend fun deleteConversationMessage(
        conversationAndMessageCrossRef: ConversationAndMessageCrossRef
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageSender(
        vararg messageCrossRef: SenderAndMessageCrossRef
    )

    @Delete
    suspend fun deleteMessageSender(
        messageCrossRef: SenderAndMessageCrossRef
    )
}