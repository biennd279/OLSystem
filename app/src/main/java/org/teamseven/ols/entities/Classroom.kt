package org.teamseven.ols.entities

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "classrooms"
)
data class Classroom(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "classroom_id")
    @SerializedName("id")
    var id: Int,

    @ColumnInfo(name = "code")
    @SerializedName("code")
    var code: String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @ColumnInfo(name = "school")
    @SerializedName("school")
    var school: String,

    @Embedded
    @SerializedName("setting")
    var setting: ClassroomSetting? = null,
)