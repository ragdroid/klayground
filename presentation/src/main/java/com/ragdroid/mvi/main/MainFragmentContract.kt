package com.ragdroid.mvi.main

import com.ragdroid.mvi.models.MainViewState
import io.reactivex.Observable

/**
 * Created by garimajain on 19/11/17.
 */
interface MainFragmentContract {

    interface View {

        fun pullToRefreshIntent(): Observable<Boolean>
        fun loadingIntent(): Observable<Boolean>
        fun render(state: MainViewState)
    }

    interface Presenter {

    }
}