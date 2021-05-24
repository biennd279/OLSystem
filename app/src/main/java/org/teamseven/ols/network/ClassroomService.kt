package org.teamseven.ols.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.ClassroomSetting
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.responses.AllClassroomsResponse
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ClassroomService {
    @GET(Constants.CLASSROOM_URL)
    suspend fun fetchAllClassrooms(): Response<AllClassroomsResponse>

    @POST(Constants.CLASSROOM_URL)
    suspend fun createClassroom(
        @Body classroomInfoRequest: ClassroomInfoRequest
    ): Response<Classroom>

    @GET("${Constants.CLASSROOM_URL}/{class_id}")
    suspend fun fetchClassroom(
        @Path("class_id") classId: Int
    ): Response<Classroom>

    @GET("${Constants.CLASSROOM_URL}/{class_id}/owners")
    suspend fun fetchClassOwner(
        @Path("class_id") classId: Int
    ): Response<List<User>>

    @GET("${Constants.CLASSROOM_URL}/{class_id}/students")
    suspend fun fetchJoinedParticipant(
        @Path("class_id") classId: Int
    ): Response<List<User>>

    @POST("${Constants.CLASSROOM_URL}/{class_id}/students/join")
    suspend fun joinClass(
        @Path("class_id") classId: Int
    ): Response<Void>

    @POST("${Constants.CLASSROOM_URL}/{class_id}/students/leave")
    suspend fun leaveClass(
        @Path("class_id") classId: Int
    ): Response<Void>

    @POST("${Constants.CLASSROOM_URL}/join/{classroom_code}")
    suspend fun joinWithCode(
        @Path("classroom_code") classroomCode: String
    ): Response<Void>

    @DELETE("${Constants.CLASSROOM_URL}/{class_id}")
    suspend fun deleteClass(
        @Path("class_id") classId: Int
    ): Response<Void>

    @PATCH("${Constants.CLASSROOM_URL}/{class_id}")
    suspend fun update(
        @Path("class_id") classId: Int,
        @Body classroomInfoRequest: ClassroomInfoRequest
    ): Response<Classroom>

    @PATCH("${Constants.CLASSROOM_URL}/{class_id}/setting")
    suspend fun updateSetting(
        @Path("class_id") classId: Int,
        @Body classroomSetting: ClassroomSetting
    ): Response<Classroom>

    companion object {
        fun create(token: String): ClassroomService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    )
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