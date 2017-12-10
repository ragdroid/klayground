package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.MainFragmentPresenter
import com.ragdroid.mvi.presenters.MainFragmentPresenterImpl
import dagger.Binds
import dagger.Module

/**
 * Created by garimajain on 22/11/17.
 */
@Module
abstract class MainFragmentModule {

    @Binds
    @FragmentScope
    abstract fun
            mainFragmentPresenter(presenter: MainFragmentPresenterImpl): MainFragmentPresenter
}