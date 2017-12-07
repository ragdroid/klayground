package com.ragdroid.mvi.dagger

import com.ragdroid.api.dagger.ApiModule
import com.ragdroid.data.DataModule
import com.ragdroid.mvi.RagdroidApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by garimajain on 18/11/17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class, ApiModule::class,
        ActivityBindingModule::class, AndroidSupportInjectionModule::class))
interface AppComponent: AndroidInjector<RagdroidApplication> {
}