package com.ragdroid.mvi.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fueled.reclaim.ItemsViewAdapter
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.models.MainViewState
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : DaggerFragment(), MainFragmentContract.View {

    lateinit @Inject var presenter: MainFragmentContract.Presenter
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter = ItemsViewAdapter(getContext())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter

    }

    override fun pullToRefreshIntent(): Observable<Boolean>  =
            binding.refreshLayout.refreshes().map { ignored -> true }

    override fun loadingIntent(): Observable<Boolean> = Observable.just(true)

    override fun render(state: MainViewState) {

    }

}
