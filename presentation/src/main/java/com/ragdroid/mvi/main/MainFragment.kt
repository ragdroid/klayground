package com.ragdroid.mvi.main

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fueled.reclaim.ItemPresenterProvider
import com.fueled.reclaim.ItemsViewAdapter
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.FragmentMainBinding
import com.ragdroid.mvi.helpers.BindFragment
import com.ragdroid.mvi.items.CharacterItem
import com.ragdroid.mvi.models.CharacterItemPresenter
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : DaggerFragment(), MainFragmentView, ItemPresenterProvider<CharacterItemPresenter>, CharacterItemPresenter {

    lateinit @Inject var presenter: MainFragmentPresenter
    //delegate the binding initialization to BindFragment delegate
    private val binding: FragmentMainBinding by BindFragment(R.layout.fragment_main)
    private val adapter: ItemsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemsViewAdapter(context)
    }
    val descriptionClickSubject: PublishSubject<MainAction.LoadDescription> = PublishSubject.create()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root


    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = androidx.recyclerview.widget.LinearLayoutManager(context)
        val decoration = androidx.recyclerview.widget.DividerItemDecoration(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
        manager.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        binding.listView.layoutManager = manager
        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(decoration)
    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
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
            state.pullToRefreshError != null -> return@render

            state.loadingError != null -> {
                adapter.clearAllRecyclerItems()
                return@render
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
