package com.ragdroid.mvi.dagger

import com.ragdroid.data.entity.AppConfig
import com.ragdroid.mvi.BuildConfig
import dagger.Module
import dagger.Provides

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

}