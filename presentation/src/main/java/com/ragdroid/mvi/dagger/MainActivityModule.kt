package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.MainActivityContract
import com.ragdroid.mvi.main.MainActivityFragment
import com.ragdroid.mvi.presenters.MainActivityPresenter
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
    abstract fun presenter(presenter: MainActivityPresenter): MainActivityContract.Presenter

    @ContributesAndroidInjector(modules = arrayOf(MainFragmentModule::class))
    @FragmentScope
    internal abstract fun mainActivityFragment(): MainActivityFragment
}