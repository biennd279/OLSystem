package org.teamseven.ols.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.crossref.ClassroomAndConversationCrossRef
import org.teamseven.ols.entities.crossref.ConversationAndMemberCrossRef
import org.teamseven.ols.entities.db.ConversationWithMembers
import org.teamseven.ols.entities.db.ConversationWithMessage

@Dao
interface ConversationDao {

    @Query("select * from conversations")
    fun getAllConversations(): Flow<List<Conversation>>

    @Query("select * from conversations where classroom_id = :classroomId")
    fun getAllConversationsInClassroom(classroomId: Int): Flow<List<Conversation>>

    @Query("select * from conversations where conversation_id = :conversationId")
    fun getConversation(conversationId: Int): Flow<Conversation>

    @Transaction
    @Query("select * from conversations where conversation_id = :conversation_id")
    fun getConversationMessages(
        conversation_id: Int
    ): Flow<ConversationWithMessage>


    @Transaction
    @Query("select * from conversations where conversation_id = :conversation_id")
    fun getConversationMembers(
        conversation_id: Int
    ): Flow<ConversationWithMembers>

    @Insert
    suspend fun insert(vararg conversation: Conversation)

    @Delete
    suspend fun delete(conversation: Conversation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassroomConversation(
        vararg classroomAndConversationCrossRef: ClassroomAndConversationCrossRef
    )

    @Delete
    suspend fun deleteClassroomConversation(
        classroomAndConversationCrossRef: ClassroomAndConversationCrossRef
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassroomMember(
        vararg conversationAndMemberCrossRef: ConversationAndMemberCrossRef
    )

    @Delete
    suspend fun deleteClassroomMember(
        conversationAndMemberCrossRef: ConversationAndMemberCrossRef
    )
}