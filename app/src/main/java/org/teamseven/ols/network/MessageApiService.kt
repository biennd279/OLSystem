package org.teamseven.ols.network

import android.content.Context
import androidx.room.Dao
import okhttp3.OkHttpClient
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

@Dao
interface MessageApiService {
    @GET("${Constants.MESSAGE_URL}/{conversation_id}")
    fun getAllMessage(conversation_id: Int): Response<List<Message>>

    @GET("${Constants.USER_URL}/conversations")
    fun getAllConversation(): Response<List<Conversation>>

    companion object {
        fun create(token: String): MessageApiService? {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.request()
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

        fun create(context: Context): MessageApiService? {
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