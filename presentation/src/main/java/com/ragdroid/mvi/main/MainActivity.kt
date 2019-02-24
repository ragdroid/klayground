package com.ragdroid.mvi.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.ActivityMainBinding
import com.ragdroid.mvi.helpers.BindActivity
import com.ragdroid.mvi.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), MainView {


    val binding: ActivityMainBinding by BindActivity(R.layout.activity_main)

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        openFragment()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "presenter $viewModel added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun openFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fraggy = MainFragment()
        fragmentTransaction.add(R.id.fragment_container, fraggy)
        fragmentTransaction.commit()
    }

}


