package com.ragdroid.mvi.dagger

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ragdroid.data.base.Helpers
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.data.entity.AppConfig
import com.ragdroid.mvi.BuildConfig
import com.ragdroid.mvi.MarvelApplication
import com.ragdroid.mvi.helpers.AndroidHelpers
import com.ragdroid.mvi.helpers.SchedulersProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by garimajain on 18/11/17.
 */
@Module
class AppModule {

    @Provides
    fun provideMarvelConfig(): AppConfig {
        return AppConfig(BuildConfig.BASE_URL,
                BuildConfig.PUBLIC_KEY,
                BuildConfig.PRIVATE_KEY)
    }


    @Singleton
    @Provides
    fun provideGlide(application: MarvelApplication): RequestManager {
        return Glide.with(application)
    }

    @Provides
    fun provideHelpers(helper: AndroidHelpers): Helpers {
        return helper
    }

    @Provides
    fun provideSchedulers(providerImpl: SchedulersProviderImpl): SchedulerProvider {
        return providerImpl
    }

}