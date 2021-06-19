package org.teamseven.ols.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.teamseven.ols.network.AuthInterceptor
import org.teamseven.ols.utils.DataConverterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    @Named("WithSessionManager")
    fun provideOkHttpClient(context: Context) = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context))
        .build()

    @Provides
    @Singleton
    fun provideDataConverterFactory() = DataConverterFactory()

    @Provides
    @Singleton
    fun provideGsonConverterFactory() = GsonConverterFactory.create()


}