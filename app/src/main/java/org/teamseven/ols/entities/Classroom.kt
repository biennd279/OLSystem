package org.teamseven.ols.entities

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "classrooms"
)
data class Classroom(
    @PrimaryKey(autoGenerate = false)
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
    var setting: ClassroomSetting?,

    //TODO mapping many type of classroom
//    @ColumnInfo(name = "type_classroom")
//    @Expose(deserialize = false, serialize = false)
//    @TypeConverters(TypeOfClass.Converter::class)
//    var typeClassroom: TypeOfClass = TypeOfClass.Joined


) {
//    enum class TypeOfClass {
//        Own, Joined;
//
//        object Converter {
//            @TypeConverter
//            fun toInt(typeClassroom: TypeOfClass) = typeClassroom.ordinal
//
//            @TypeConverter
//            fun fromInt(int: Int) = values()[int]
//        }
//    }
}