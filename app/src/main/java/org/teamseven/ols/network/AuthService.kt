package org.teamseven.ols.network

import android.content.Context
import okhttp3.OkHttpClient
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body login: LoginRequest) : Call<LoginResponse>

    companion object {
            fun create(context: Context): AuthService {
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(context))
                    .build()

                return Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(DataConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(AuthService::class.java)
            }

            fun create() : AuthService {
                return Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(DataConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AuthService::class.java)
            }
    }
}