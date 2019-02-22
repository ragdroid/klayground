package com.ragdroid.mvi.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.ActivityMainBinding
import com.ragdroid.mvi.helpers.BindActivity
import com.ragdroid.mvi.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity

import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainView {


    val binding: ActivityMainBinding by BindActivity(R.layout.activity_main)

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "presenter $viewModel added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}


