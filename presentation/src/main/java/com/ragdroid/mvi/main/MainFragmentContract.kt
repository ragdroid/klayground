package com.ragdroid.mvi.main

import com.ragdroid.mvi.base.Presenter
import com.ragdroid.mvi.base.View
import io.reactivex.Observable

/**
 * Created by garimajain on 19/11/17.
 */
interface MainFragmentView : View {

    /**
     * Expose various intents
     */
    fun pullToRefreshIntent(): Observable<Boolean>
    fun loadingIntent(): Observable<Boolean>
    fun render(state: MainViewState)

}

interface MainFragmentPresenter : Presenter<MainFragmentView>
