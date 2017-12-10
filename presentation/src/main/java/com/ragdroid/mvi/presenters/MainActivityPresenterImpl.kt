package com.ragdroid.mvi.presenters

import com.ragdroid.mvi.main.MainPresenter
import com.ragdroid.mvi.main.MainView
import javax.inject.Inject

/**
 * Created by garimajain on 19/11/17.
 */
class MainActivityPresenterImpl
    @Inject constructor() : MainPresenter {

    override fun attachView(view: MainView) {

    }

    override fun detachView() {
    }
}