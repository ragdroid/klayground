package com.ragdroid.mvi.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ragdroid.mvi.R
import com.ragdroid.mvi.characters.CharactersFragment
import com.ragdroid.mvi.databinding.ActivityMainBinding
import com.ragdroid.mvi.helpers.BindActivity
import com.ragdroid.mvi.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {


    val binding: ActivityMainBinding by BindActivity(R.layout.activity_main)

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        openFragment()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "presenter $viewModel added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun openFragment() {
        val fragmentManager = supportFragmentManager

        val fragment = supportFragmentManager.findFragmentByTag("fraggy")
        if (fragment == null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
//        val fraggy = MainFragment()
            val fraggy = CharactersFragment()
            fragmentTransaction.replace(R.id.fragment_container, fraggy, "fraggy")
            fragmentTransaction.commit()
        }
    }

}


