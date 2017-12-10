package com.ragdroid.mvi.base

/**
 * Created by garima-fueled on 06/12/17.
 */
interface Presenter<T: View> {

    fun attachView(view: T)
    fun detachView()

}