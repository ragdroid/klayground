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
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.models.CharacterModel
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : DaggerFragment(), MainFragmentView {

    lateinit @Inject var presenter: MainFragmentPresenter
    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private lateinit var adapter: ItemsViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        adapter = ItemsViewAdapter(context)
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter

    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }

    override fun pullToRefreshIntent(): Observable<Boolean>  =
            binding.refreshLayout.refreshes().map { ignored -> true }

    override fun loadingIntent(): Observable<Boolean> = Observable.just(true)

    override fun render(state: MainViewState) {
        binding.model = state
        when {
            state.pullToRefreshError != null -> return@render

            state.loadingError != null -> {
                adapter.clearAllRecyclerItems()
                return@render
            }

            else -> {
                adapter.clearAllRecyclerItems()
                val characterModelList =
                        state.characters.map { CharacterItem(CharacterModel(it.name, it.imageUrl)) }
                adapter.addItemsList(characterModelList)
            }
        }
    }

}
