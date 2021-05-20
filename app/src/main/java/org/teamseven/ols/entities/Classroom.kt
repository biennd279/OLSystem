package org.teamseven.ols.entities

import com.google.gson.annotations.SerializedName

data class Classroom(
    @SerializedName("id")
    var id: Int,
    @SerializedName("code")
    var code: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("school")
    var school: String,
    @SerializedName("setting")
    var setting: ClassroomSetting?,
)