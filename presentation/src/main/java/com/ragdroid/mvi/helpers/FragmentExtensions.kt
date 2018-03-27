package com.ragdroid.mvi.helpers

import android.support.v4.app.Fragment
import android.widget.Toast

/**
 * Created by garimajain on 27/03/18.
 */
fun Fragment.showToast(message: String) =
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()