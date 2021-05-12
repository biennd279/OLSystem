package org.teamseven.ols.entities

import com.google.gson.annotations.SerializedName

data class Data<T>(
    @SerializedName("data")
    val data: T? = null,
)
