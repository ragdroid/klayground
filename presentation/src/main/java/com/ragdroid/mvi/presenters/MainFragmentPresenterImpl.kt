package com.ragdroid.mvi.presenters

import com.ragdroid.data.MainRepository
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainFragmentPresenter
import com.ragdroid.mvi.main.MainFragmentView
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.*
import javax.inject.Inject

/**
 * Created by garimajain on 19/11/17.
 */
class MainFragmentPresenterImpl
@Inject constructor(private val repository: MainRepository,
                    private val schedulerProvider: SchedulerProvider,
                    private val resources: ResourceProvider) : MainFragmentPresenter {

    var disposable: Disposable? = null

    var view: MainFragmentView? = null

    override fun attachView(view: MainFragmentView) {
        this.view = view

        val loadingResult = loadingResult(view)

        val pullToRefreshResult = pullToRefreshResult(view)

        val descriptionResult = loadDescriptionResult(view)

        val allIntentObservable = Observable.merge(loadingResult, pullToRefreshResult, descriptionResult)
        val initialState = MainViewState.init()


        disposable = allIntentObservable
                .scan(initialState) { state, result -> state.reduce(result, resources)}
                .observeOn(schedulerProvider.ui())
                .subscribe (
                        { state ->
                            Timber.d("Got state $state")
                            view.render(state) },
                        {e -> Timber.e(e)})


    }

    private fun loadDescriptionResult(view: MainFragmentView): Observable<MainResult>? {
        return view.loadDescription()
                .observeOn(schedulerProvider.io())
                .flatMap { action ->
                    repository.fetchCharacter(action.characterId).toObservable()
                            .delay(2000, TimeUnit.MILLISECONDS, schedulerProvider.computation())
                            .map { item ->
                                MainResult.DescriptionResult.DescriptionLoadComplete(item.id, item.description) as MainResult
                            }
                            .startWith(MainResult.DescriptionResult.DescriptionLoading(action.characterId))
                            .onErrorReturn { error ->
                                Timber.e(error)
                                MainResult.DescriptionResult.DescriptionError(action.characterId, error)
                            }
                }
    }

    private fun pullToRefreshResult(view: MainFragmentView): Observable<MainResult>? {
        return view.pullToRefreshIntent()
                .observeOn(schedulerProvider.io())
                .flatMap { ignored ->
                    repository.fetchCharacters().toObservable()
                            .map { items -> MainResult.PullToRefreshComplete(items) as MainResult }
                            .startWith(MainResult.PullToRefreshing)
                            .onErrorReturn { error -> MainResult.PullToRefreshError(error) }
                }
    }

    private fun loadingResult(view: MainFragmentView): Observable<MainResult>? {
        return view.loadingIntent()
                .observeOn(schedulerProvider.io())
                .flatMap { ignored -> repository.fetchCharacters().toObservable() }
                .map { states -> MainResult.LoadingComplete(states) as MainResult }
                .startWith(MainResult.Loading)
                .onErrorReturn { error -> MainResult.LoadingError(error) }
    }

    override fun detachView() {
        disposable?.dispose()
        disposable = null
        view = null
    }

}