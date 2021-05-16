package org.teamseven.ols.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.ClassroomSetting
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.responses.AllClassroomsResponse
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ClassroomService {
    @GET(Constants.CLASSROOM_URL)
    fun fetchAllClassrooms(): Call<AllClassroomsResponse>

    @POST(Constants.CLASSROOM_URL)
    fun createClassroom(
        @Body classroomInfoRequest: ClassroomInfoRequest
    ) : Call<Classroom>

    @GET("${Constants.CLASSROOM_URL}/{class_id}")
    fun fetchClassroom(
        @Path("class_id") classId: Int
    ): Call<Classroom>

    @GET("${Constants.CLASSROOM_URL}/{class_id}/owners")
    fun fetchClassOwner() : Call<List<User>>

    @GET("${Constants.CLASSROOM_URL}/{class_id}/students")
    fun fetchJoinedParticipant() : Call<List<User>>

    //TODO fix api join class
    @POST("${Constants.CLASSROOM_URL}/{class_id}/students/join")
    fun joinClass(
        @Path("class_id") classId: Int
    )

    //TODO fix api leave class
    @POST("${Constants.CLASSROOM_URL}/{class_id}/students/join")
    fun leaveClass(
        @Path("class_id") classId: Int
    )

    //TODO fix api delete class
    @DELETE("${Constants.CLASSROOM_URL}/{class_id}")
    fun deleteClass(
        @Path("class_id") classId: Int
    ) : Call<Void>

    @PATCH("${Constants.CLASSROOM_URL}/{class_id}")
    fun update(
        @Path("class_id") classId: Int,
        @Body classroomInfoRequest: ClassroomInfoRequest
    ) : Call<Classroom>

    @PATCH("${Constants.CLASSROOM_URL}/{class_id}/setting")
    fun updateSetting(
        @Path("class_id") classId: Int,
        @Body classroomSetting: ClassroomSetting
    ) : Call<Classroom>

    companion object {
        fun create(token: String): ClassroomService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request: Request =
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    chain.proceed(request)
                })
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(DataConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ClassroomService::class.java)
        }

        fun create(context: Context): ClassroomService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(DataConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ClassroomService::class.java)
        }
    }
}