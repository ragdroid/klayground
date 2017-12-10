package com.ragdroid.mvi.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.ActivityMainBinding
import com.ragdroid.mvi.helpers.BindActivity
import dagger.android.support.DaggerAppCompatActivity

import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainView {

    @Inject lateinit var presenter: MainPresenter

    val binding: ActivityMainBinding by BindActivity(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "presenter $presenter injected", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}


