package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.MainFragmentContract
import com.ragdroid.mvi.presenters.MainFragmentPresenter
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
            mainFragmentPresenter(presenter: MainFragmentPresenter): MainFragmentContract.Presenter
}