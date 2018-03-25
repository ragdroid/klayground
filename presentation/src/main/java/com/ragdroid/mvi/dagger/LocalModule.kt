package com.ragdroid.mvi.dagger

import android.os.StrictMode
import com.ragdroid.api.LocalApiModule
import dagger.Module

/**
 * Created by garimajain on 25/03/18.
 */

@Module
class LocalModule : LocalApiModule() {
    private var threadPolicyUpdated: Boolean = false

     // Disable Network strict mode because we will be using a mock server
     override fun mockApiUrl(): String {
            if (!threadPolicyUpdated) {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                threadPolicyUpdated = true
            }
            return mockWebServer.url("/").toString()
        }
}