package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by garimajain on 19/11/17.
 */
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @ActivityScope
    internal abstract fun mainActivity(): MainActivity

}