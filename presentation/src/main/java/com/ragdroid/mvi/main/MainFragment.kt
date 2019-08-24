package com.ragdroid.mvi.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ragdroid.mvvmi.core.NavigationState
import com.fueled.reclaim.ItemPresenterProvider
import com.fueled.reclaim.ItemsViewAdapter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.helpers.SpaceItemDecoration
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.models.CharacterItemPresenter
import com.ragdroid.mvi.viewmodel.MainFragmentViewModel
import com.ragdroid.mvvmi.core.MviView
import dagger.android.support.DaggerFragment
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import timber.log.Timber
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : DaggerFragment(),
        ItemPresenterProvider<CharacterItemPresenter>,
        CharacterItemPresenter, MviView<MainAction, MainViewState> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter: ItemsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemsViewAdapter(context)
    }

    override lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setTitle(R.string.title_rx)
        val manager = LinearLayoutManager(context)
        val decoration = SpaceItemDecoration(resources.getDimensionPixelOffset(R.dimen.keyline_1), false, false, true, false)
        manager.orientation = RecyclerView.VERTICAL
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter
        binding.listView.itemAnimator = FadeInAnimator()
        binding.listView.addItemDecoration(decoration)
        setupViewModel()
        super.onMviViewCreated(savedInstanceState)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainFragmentViewModel::class.java)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.onAction(MainAction.PullToRefresh)
        }
        viewModel.onAction(MainAction.LoadData)
    }

    private val descriptionClickProcessor = PublishProcessor.create<MainAction.LoadDescription>()

    override fun onCharacterDescriptionClicked(itemId: Long) {
        viewModel.onAction(MainAction.LoadDescription(itemId))
    }

    override fun getItemPresenter(): CharacterItemPresenter {
        return this
    }


    private fun loadingIntent(): Flowable<MainAction.LoadData> = Flowable.just(MainAction.LoadData)

    private fun loadDescription(): Flowable<MainAction.LoadDescription> {
        return descriptionClickProcessor
    }

    override fun render(state: MainViewState) {
        Timber.d("got state $state")
        binding.refreshing = state.loadingState == MainViewState.LoadingState.PullToRefreshing
        binding.loading = state.loadingState == MainViewState.LoadingState.Loading
        val characterModelList =
                state.characters.map {
                    CharacterItem(it, this)
                }
        adapter.replaceItems(characterModelList, true)
    }

    override val lifecycleOwner: LifecycleOwner
        get() = this


    override fun navigate(navigationState: NavigationState) {
        when (navigationState) {
            is MainNavigation.Snackbar -> Snackbar.make(binding.root, navigationState.message, Snackbar.LENGTH_SHORT).show()
            else -> {//do nothing
            }
        }
    }

}
