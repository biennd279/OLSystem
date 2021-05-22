package org.teamseven.ols.entities.responses

import com.google.gson.annotations.SerializedName
import org.teamseven.ols.entities.User

data class LoginResponse(
    @SerializedName("user")
    var user: User,
    @SerializedName("token")
    var token: String,
) {

}
