package com.ragdroid.data

import com.ragdroid.api.dagger.ApiConfig
import com.ragdroid.data.entity.AppConfig
import dagger.Module
import dagger.Provides

/**
 * Created by garimajain on 18/11/17.
 */
@Module
class DataModule {

    @Provides
    fun provideApiConfig(appConfig: AppConfig): ApiConfig {
        return ApiConfig(appConfig.baseUrl)
    }

    @Provides
    fun provideMainRepository(repository: MainRepositoryImpl): MainRepository {
        return repository
    }

}