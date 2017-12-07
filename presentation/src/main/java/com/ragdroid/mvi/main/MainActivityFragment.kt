package com.ragdroid.mvi.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import dagger.android.support.DaggerFragment

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : DaggerFragment() {

    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }
}
