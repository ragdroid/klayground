package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by garimajain on 19/11/17.
 */
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector()
    @FragmentScope
    internal abstract fun mainActivityFragment(): MainFragment
}