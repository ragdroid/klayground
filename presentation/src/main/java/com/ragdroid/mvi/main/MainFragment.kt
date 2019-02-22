package com.ragdroid.mvi.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.fueled.reclaim.ItemPresenterProvider
import com.fueled.reclaim.ItemsViewAdapter
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.models.CharacterItemPresenter
import com.ragdroid.mvi.viewmodel.MainFragmentViewModel
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : DaggerFragment(), MainFragmentView, ItemPresenterProvider<CharacterItemPresenter>, CharacterItemPresenter {

    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter: ItemsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemsViewAdapter(context)
    }
    val descriptionClickSubject: PublishSubject<MainAction.LoadDescription> = PublishSubject.create()

    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
        viewModel.processActions(Observable.merge(pullToRefreshIntent(), descriptionClickSubject, loadingIntent()))

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = androidx.recyclerview.widget.LinearLayoutManager(context)
        val decoration = androidx.recyclerview.widget.DividerItemDecoration(context, RecyclerView.VERTICAL)
        manager.orientation = RecyclerView.VERTICAL
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(decoration)
    }

    override fun onCharacterDescriptionClicked(itemId: Long) {
        descriptionClickSubject.onNext(MainAction.LoadDescription(itemId))
    }

    override fun getItemPresenter(): CharacterItemPresenter {
        return this
    }


    override fun pullToRefreshIntent(): Observable<MainAction.PullToRefresh>  =
            binding.refreshLayout.refreshes().map { action -> MainAction.PullToRefresh }

    override fun loadingIntent(): Observable<MainAction.LoadData> = Observable.just(MainAction.LoadData)

    override fun loadDescription(): Observable<MainAction.LoadDescription> {
        return descriptionClickSubject
    }

    override fun render(state: MainViewState) {
        binding.model = state
        when {
            state.pullToRefreshError != null -> return

            state.loadingError != null -> {
                adapter.clearAllRecyclerItems()
                return
            }

            else -> {
                val characterModelList =
                        state.characters.map {
                            CharacterItem(it, this)
                        }
                adapter.replaceItems(characterModelList, true)
            }
        }
    }

}
