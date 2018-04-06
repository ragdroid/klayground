package com.ragdroid.mvi.dagger

import com.ragdroid.api.dagger.ApiModule
import com.ragdroid.data.DataModule
import com.ragdroid.mvi.MarvelApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by garimajain on 18/11/17.
 */
@Singleton
@Component(modules = [AppModule::class, DataModule::class,
                        ApiModule::class, ActivityBindingModule::class,
                        AndroidSupportInjectionModule::class])
interface AppComponent: AndroidInjector<MarvelApplication> {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(application: MarvelApplication): Builder
    }

}