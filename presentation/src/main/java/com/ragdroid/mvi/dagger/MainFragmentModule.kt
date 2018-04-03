package com.ragdroid.mvi.dagger

import com.ragdroid.mvi.main.Presenter
import com.ragdroid.mvi.presenters.PresenterImpl
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
            mainFragmentPresenter(presenter: PresenterImpl): Presenter
}