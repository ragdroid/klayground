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
import com.google.android.material.snackbar.Snackbar
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.helpers.SpaceItemDecoration
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainNavigation
import com.ragdroid.mvi.main.MainViewState
import com.ragdroid.mvi.models.CharacterItemPresenter
import com.ragdroid.mvvmi.core.NavigationState
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject
import jp.wasabeef.recyclerview.animators.FadeInAnimator

/**
 * A placeholder fragment containing a simple view.
 */
class CharactersFragment : DaggerFragment(),
        ItemPresenterProvider<CharacterItemPresenter>,
        CharacterItemPresenter {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter: ItemsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemsViewAdapter(context)
    }


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
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.navigationLiveData.observe(viewLifecycleOwner, Observer { navigate(it) })
        if (savedInstanceState == null) {
            viewModel.processActions()
            viewModel.onAction(MainAction.LoadData)
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.onAction(MainAction.PullToRefresh)
        }
    }




    override fun getItemPresenter(): CharacterItemPresenter {
        return this
    }

    override fun onCharacterDescriptionClicked(itemId: Long) {
        viewModel.onAction(MainAction.LoadDescription(itemId))
    }

    fun render(state: MainViewState) {
        Timber.d("got state $state")
        binding.refreshing = state.loadingState == MainViewState.LoadingState.PullToRefreshing
        binding.loading = state.loadingState == MainViewState.LoadingState.Loading
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

}
