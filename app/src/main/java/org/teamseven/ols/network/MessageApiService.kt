package org.teamseven.ols.network

import android.content.Context
import androidx.room.Dao
import okhttp3.OkHttpClient
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.User
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Dao
interface MessageApiService {
    @GET("${Constants.MESSAGE_URL}/{conversation_id}")
    suspend fun getConversationMessages(
        @Path("conversation_id") conversationId: Int
    ): Response<List<Message>>

    @GET("${Constants.MESSAGE_URL}/convo/{conversation_id}/detail")
    suspend fun getConversationDetail(
        @Path("conversation_id") conversationId: Int
    )

    @GET("${Constants.MESSAGE_URL}/convo/{conversation_id}/members")
    suspend fun getConversationMembers(
        @Path("conversation_id") conversationId: Int
    ): Response<List<User>>

    @GET("${Constants.USER_URL}/conversations")
    suspend fun getAllConversation(): Response<List<Conversation>>

    @GET("${Constants.USER_URL}/conversations")
    suspend fun getClassroomConversation(
        @Query("classroom_id") classroomId: Int
    ): Response<List<Conversation>>

    companion object {
        fun create(token: String): MessageApiService? {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    )
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(DataConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MessageApiService::class.java)
        }

        fun create(context: Context): MessageApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(DataConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MessageApiService::class.java)
        }
    }
}