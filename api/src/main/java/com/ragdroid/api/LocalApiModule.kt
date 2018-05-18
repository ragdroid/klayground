package com.ragdroid.api


import javax.inject.Singleton

import dagger.Module
import com.ragdroid.api.dagger.ApiModule
import dagger.Provides
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer

/**
 * Contains the `MockWebServer` logic and is extending by the `local` build variant module and is
 * also extended by `test` module. LocalApiModule is responsible for providing the appropriate URL
 * on which MockWebServer runs. It also initializes the `LocalResponseDispatcher` which selects the
 * appropriate `json` file from the `local-resources` module.
 * Created by garima-fueled on 31/03/17.
 */
@Module
abstract class LocalApiModule : ApiModule() {

    protected val mockWebServer: MockWebServer = MockWebServer()
    private val responseDispatcher: LocalResponseDispatcher = LocalResponseDispatcher()

    init {
        mockWebServer.setDispatcher(responseDispatcher)
    }

    override fun mockApiUrl(): String? = mockWebServer.url("/").toString()


    @Provides
    @Singleton
    fun provideDefaultMockWebServer(): MockWebServer {
        return mockWebServer
    }


    val testDispatcher: Dispatcher
        @Provides
        @Singleton
        get() = responseDispatcher
}
