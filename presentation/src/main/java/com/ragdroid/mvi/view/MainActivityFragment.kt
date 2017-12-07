package com.ragdroid.mvi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ragdroid.mvi.R
import dagger.android.DaggerFragment

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : DaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}
