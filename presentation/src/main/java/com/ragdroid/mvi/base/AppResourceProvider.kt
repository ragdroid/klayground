package com.ragdroid.mvi.base

import android.content.Context

/**
 * Created by garimajain on 25/03/18.
 */
class AppResourceProvider(val context: Context): ResourceProvider {

    override fun getString(resId: Int): String  = context.getString(resId)

}