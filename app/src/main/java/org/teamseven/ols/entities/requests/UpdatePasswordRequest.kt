package org.teamseven.ols.entities.requests

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("currentPass")
    var currentPass: String,

    @SerializedName("updatePass")
    val updatePass: String
)
