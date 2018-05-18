package com.ragdroid.api.dagger

import com.ragdroid.api.MarvelApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by garimajain on 18/11/17.
 */
@Module
open class ApiModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        val moshi = Moshi.Builder()
                .build()
        return moshi
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient,
                        moshi: Moshi,
                        config: ApiConfig): Retrofit {
        val apiUrl = (if (mockApiUrl() == null) config.baseUrl else {
            mockApiUrl()
        }) as String

        return Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
    }

    open fun mockApiUrl(): String? = null

    @Provides
    @Singleton
    fun provideMarvelService(retrofit: Retrofit): MarvelApi {
        return retrofit.create(MarvelApi::class.java)
    }

}

data class ApiConfig(val baseUrl: String)
