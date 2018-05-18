package com.ragdroid.mvi.base

/**
 * Created by garimajain on 25/03/18.
 */
interface ResourceProvider {
    fun getString(resId: Int): String
}