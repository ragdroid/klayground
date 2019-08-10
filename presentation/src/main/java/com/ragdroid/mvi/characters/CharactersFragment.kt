package com.ragdroid.mvi.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fueled.reclaim.ItemPresenterProvider
import com.fueled.reclaim.ItemsViewAdapter
import com.github.satoshun.coroutinebinding.androidx.swiperefreshlayout.widget.refreshes
import com.google.android.material.snackbar.Snackbar
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.helpers.SpaceItemDecoration
import com.ragdroid.mvi.helpers.merge
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainNavigation
import com.ragdroid.mvi.main.MainViewState
import com.ragdroid.mvi.models.CharacterItemPresenter
import com.ragdroid.mvvmi.core.NavigationState
import dagger.android.support.DaggerFragment
import hu.akarnokd.kotlin.flow.PublishSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import jp.wasabeef.recyclerview.animators.FadeInAnimator

/**
 * A placeholder fragment containing a simple view.
 */
class CharactersFragment : DaggerFragment(),
        ItemPresenterProvider<CharacterItemPresenter>,
        CharacterItemPresenter, CoroutineScope {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter: ItemsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemsViewAdapter(context)
    }
    //we can also use a ConflatedBroadcastChannel here
    private val descriptionClickProcessor: PublishSubject<MainAction.LoadDescription> = PublishSubject()
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    lateinit var viewModel: CharactersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setTitle(R.string.title_flow)
        val manager = LinearLayoutManager(context)
        val decoration = SpaceItemDecoration(resources.getDimensionPixelOffset(R.dimen.keyline_1), false, false, true, false)
        manager.orientation = RecyclerView.VERTICAL
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter
        binding.listView.itemAnimator = FadeInAnimator()
        binding.listView.addItemDecoration(decoration)
        setupViewModel(savedInstanceState)
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CharactersViewModel::class.java)
        viewModel.stateLiveData().observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.navigationLiveData().observe(viewLifecycleOwner, Observer { navigate(it) })
        if (savedInstanceState == null) {
            viewModel.processActions(loadingIntent().merge(pullToRefreshIntent(), loadDescription()))
        }
    }


    override fun onCharacterDescriptionClicked(itemId: Long) {
        launch {
            descriptionClickProcessor.emit(MainAction.LoadDescription(itemId))
        }
    }

    override fun getItemPresenter(): CharacterItemPresenter {
        return this
    }


    private fun pullToRefreshIntent(): Flow<MainAction.PullToRefresh> =
            binding.refreshLayout.refreshes().consumeAsFlow().map { MainAction.PullToRefresh }

    private fun loadingIntent(): Flow<MainAction.LoadData> = flow { emit(MainAction.LoadData) }

    private fun loadDescription(): Flow<MainAction.LoadDescription> {
        return descriptionClickProcessor
    }

    fun render(state: MainViewState) {
        Timber.d("got state $state")
        binding.model = state
        val characterModelList =
                state.characters.map {
                    CharacterItem(it, this)
                }
        adapter.replaceItems(characterModelList, true)
    }


    fun navigate(navigationState: NavigationState) {
        when (navigationState) {
            is MainNavigation.Snackbar -> Snackbar.make(binding.root, navigationState.message, Snackbar.LENGTH_SHORT).show()
            else -> {//do nothing
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineContext.cancel()
    }
}
