package org.teamseven.ols.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "conversations"
)
data class Conversation(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "conversation_id")
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String?,

    @ColumnInfo(name = "user_id")
    @SerializedName("creator_id")
    val creatorId: Int,

    @ColumnInfo(name = "classroom_id")
    @SerializedName("classroom_id")
    val classroomId: Int,

    @ColumnInfo(name = "type")
    @SerializedName("type")
    val type: String?,
) {
    /**
     * @property Type is type of conversation
     */
    enum class Type {
        TODO()
//        SINGLE, GROUP;
    }
}
