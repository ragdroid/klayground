package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.MainActivityFragment
import com.ragdroid.mvi.main.MainPresenter
import com.ragdroid.mvi.presenters.MainActivityPresenterImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by garimajain on 19/11/17.
 */
@Module
abstract class MainActivityModule {

    @Binds
    @ActivityScope
    abstract fun presenter(presenter: MainActivityPresenterImpl): MainPresenter

    @ContributesAndroidInjector(modules = arrayOf(MainFragmentModule::class))
    @FragmentScope
    internal abstract fun mainActivityFragment(): MainActivityFragment
}