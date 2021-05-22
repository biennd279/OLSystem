package org.teamseven.ols.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("avatar_url")
    var avatarUrl: String?,
    @SerializedName("role")
    var role: String?
    ){

}
