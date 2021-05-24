package org.teamseven.ols.entities.requests

import com.google.gson.annotations.SerializedName

data class ClassroomInfoRequest(
    @SerializedName("name")
    var name: String,

    @SerializedName("school")
    var school: String
)
