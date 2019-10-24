package com.ragdroid.mvi.dagger

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ragdroid.data.base.Helpers
import com.ragdroid.data.entity.AppConfig
import com.ragdroid.mvi.BuildConfig
import com.ragdroid.mvi.MarvelApplication
import com.ragdroid.mvi.base.AppResourceProvider
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.helpers.AndroidHelpers
import com.ragdroid.mvi.helpers.DispatchProvider
import com.ragdroid.mvi.helpers.DispatchProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by garimajain on 18/11/17.
 */
@Module
class AppModule {

    @Provides
    fun provideMarvelConfig(): AppConfig =
            AppConfig(BuildConfig.BASE_URL,
                BuildConfig.PUBLIC_KEY,
                BuildConfig.PRIVATE_KEY)


    @Singleton
    @Provides
    fun provideGlide(application: MarvelApplication): RequestManager = Glide.with(application)

    @Provides
    fun provideHelpers(helper: AndroidHelpers): Helpers = helper

    @Provides
    fun provideResourceProvider(application: MarvelApplication): ResourceProvider =
            AppResourceProvider(application.applicationContext)

    @Provides
    fun provideDispatchers(dispatchProviderImpl: DispatchProviderImpl): DispatchProvider = dispatchProviderImpl

}