package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.view.MainActivity
import com.ragdroid.mvi.view.MainActivityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by garimajain on 19/11/17.
 */
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    @ActivityScope
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun mainActivityFragment(): MainActivityFragment
}