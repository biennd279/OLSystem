package org.teamseven.ols.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.requests.UpdatePasswordRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET("${Constants.USER_URL}/profile")
    suspend fun getProfile(): Response<User>

    @POST("${Constants.USER_URL}/password")
    suspend fun updatePassword(
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): Response<Void>

    @GET("${Constants.USER_URL}/validate")
    suspend fun refreshToken(): Response<LoginResponse>

    companion object {
        fun create(token: String): UserService? {
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
                .create(UserService::class.java)
        }

        fun create(context: Context): UserService? {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(DataConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(UserService::class.java)
        }
    }
}