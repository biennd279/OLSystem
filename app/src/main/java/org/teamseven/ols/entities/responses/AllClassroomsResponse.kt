package org.teamseven.ols.entities.responses

import com.google.gson.annotations.SerializedName
import org.teamseven.ols.entities.Classroom

data class AllClassroomsResponse(
    @SerializedName("owner")
    var listClassOwner: List<Classroom>,
    @SerializedName("joined")
    var listClassJoined: List<Classroom>,
) {

}