package com.ragdroid.mvi.main

import com.ragdroid.mvi.base.BasePresenter
import com.ragdroid.mvi.base.BaseView
import io.reactivex.Observable

/**
 * Created by garimajain on 19/11/17.
 */
interface View : BaseView {

    /**
     * Expose various intents
     */
    fun pullToRefreshIntent(): Observable<Boolean>
    fun loadingIntent(): Observable<Boolean>

    fun render(state: State)

}

interface Presenter : BasePresenter<View>
