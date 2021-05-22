package org.teamseven.ols.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @ColumnInfo(name = "email")
    @SerializedName("email")
    var email: String,

    @ColumnInfo(name = "avatar_url")
    @SerializedName("avatar_url")
    var avatarUrl: String?,

    @ColumnInfo(name = "role")
    @SerializedName("role")
    var role: String?
) {
    enum class Role {
        //TODO add role enum
    }
}
