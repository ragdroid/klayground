package com.fueled.mvp.core.mvp

import android.os.Bundle

/**
 * Created by garima on 22/02/2019.
 */
sealed class NavigationState {

    object Results {
        const val CANCEL = 0
        const val OK = 1
    }

    data class DisplayScreen(val screenName: String, val data: Bundle? = null) : NavigationState()

    data class Snackbar(val message: String) : NavigationState()

    open class Close : NavigationState()

}

