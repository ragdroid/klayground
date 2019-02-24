package com.ragdroid.mvi.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fueled.mvp.core.mvp.NavigationState
import com.fueled.reclaim.ItemPresenterProvider
import com.fueled.reclaim.ItemsViewAdapter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.models.CharacterItemPresenter
import com.ragdroid.mvi.viewmodel.MainFragmentViewModel
import com.ragdroid.mvvmi.core.MviView
import dagger.android.support.DaggerFragment
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment() : DaggerFragment(),
        ItemPresenterProvider<CharacterItemPresenter>,
        CharacterItemPresenter, MviView<MainAction, MainViewState> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter: ItemsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemsViewAdapter(context)
    }
    private val descriptionClickSubject: PublishProcessor<MainAction.LoadDescription> = PublishProcessor.create()

    override lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        manager.orientation = RecyclerView.VERTICAL
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(decoration)
        setupViewModel()
        super.onMviViewCreated()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainFragmentViewModel::class.java)
        viewModel.processActions(Flowable.merge(pullToRefreshIntent(), loadDescription(), loadingIntent()))
    }


    override fun onCharacterDescriptionClicked(itemId: Long) {
        descriptionClickSubject.onNext(MainAction.LoadDescription(itemId))
    }

    override fun getItemPresenter(): CharacterItemPresenter {
        return this
    }


    private fun pullToRefreshIntent(): Flowable<MainAction.PullToRefresh> =
        binding.refreshLayout.refreshes().toFlowable(BackpressureStrategy.DROP).map { MainAction.PullToRefresh }

    private fun loadingIntent(): Flowable<MainAction.LoadData> = Flowable.just(MainAction.LoadData)

    private fun loadDescription(): Flowable<MainAction.LoadDescription> {
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

    override val actionsSubject: PublishProcessor<MainAction> = PublishProcessor.create()
    override val lifecycleOwner: LifecycleOwner
    get() = viewLifecycleOwner


    override fun navigate(navigationState: NavigationState) {
        when (navigationState) {
            is NavigationState.Snackbar -> Snackbar.make(binding.root, navigationState.message, Snackbar.LENGTH_SHORT)
            else -> {//do nothing
                 }
        }
    }

}
