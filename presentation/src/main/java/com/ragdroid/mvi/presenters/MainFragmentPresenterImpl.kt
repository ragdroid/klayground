package com.ragdroid.mvi.presenters

import com.ragdroid.data.MainRepository
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.mvi.main.MainFragmentPresenter
import com.ragdroid.mvi.main.MainFragmentView
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by garimajain on 19/11/17.
 */
class MainFragmentPresenterImpl
@Inject constructor(private val repository: MainRepository,
                    private val schedulerProvider: SchedulerProvider) : MainFragmentPresenter {

    val subject: PublishSubject<MainResult> = PublishSubject.create()
    var disposable: Disposable? = null

    var view: MainFragmentView? = null

    override fun attachView(view: MainFragmentView) {
        this.view = view
        val loadingPartialResult = subscribeToLoading(view)

        val pullToRefreshPartialResult = subscribeToPullToRefresh(view)

        val allIntentObservable = Observable.merge(loadingPartialResult, pullToRefreshPartialResult)
        val initialState = MainViewState.init()


        disposable = allIntentObservable
                .scan(initialState) { state, result -> reducer(state, result)}
                .observeOn(schedulerProvider.ui())
                .subscribe (
                        { state ->
                            Timber.d("Got state $state")
                            view.render(state) },
                        {e -> Timber.e(e)})


    }

    private fun subscribeToPullToRefresh(view: MainFragmentView): Observable<MainResult>? {
        val pullToRefreshPartialResult = view.pullToRefreshIntent()
                .flatMap {
                    ignored -> repository.fetchCharacters().toObservable()
                        .subscribeOn(schedulerProvider.io())
                        .map { items -> MainResult.PullToRefreshComplete(items) as MainResult }
                        .startWith(MainResult.PullToRefreshing)
                }
                .onErrorReturn { error -> MainResult.PullToRefreshError(error) }
        return pullToRefreshPartialResult
    }

    private fun subscribeToLoading(view: MainFragmentView): Observable<MainResult>? {
        val loadingPartialResult = view.loadingIntent()
                .flatMap { ignored -> repository.fetchCharacters().toObservable()
                        .subscribeOn(schedulerProvider.io())
                        .map { items ->
                            if (items.size == 0)
                                MainResult.LoadingEmpty
                            else
                                MainResult.LoadingComplete(items)
                        }
                }
                .onErrorReturn { error -> MainResult.LoadingError(error) }
        return loadingPartialResult
    }


    private fun reducer(previousState: MainViewState, result: MainResult): MainViewState =
            when (result) {
                is MainResult.Loading -> previousState.copy(
                        loading = true,
                        loadingError = null)
                is MainResult.LoadingError -> previousState.copy(
                        loading = false,
                        loadingError = result.throwable)
                is MainResult.LoadingComplete -> previousState.copy(
                        loading = false,
                        loadingError = null,
                        characters = result.characters)
                is MainResult.LoadingEmpty -> previousState.copy(
                        loading = false,
                        loadingError = null,
                        characters = emptyList(),
                        emptyStateVisible = true)
                is MainResult.PullToRefreshing -> previousState.copy(
                        loading = false,
                        pullToRefreshing = true,
                        pullToRefreshError = null)
                is MainResult.PullToRefreshError -> previousState.copy(
                        pullToRefreshing = false,
                        pullToRefreshError = result.throwable)
                is MainResult.PullToRefreshComplete -> previousState.copy(
                        pullToRefreshing = false,
                        pullToRefreshError = null,
                        characters = result.characters)
                is MainResult.PullToRefreshEmpty -> previousState.copy(
                        pullToRefreshing = false,
                        loadingError = null,
                        characters = emptyList(),
                        emptyStateVisible = true
                )
            }

    override fun detachView() {
        disposable?.dispose()
        disposable = null
        view = null
    }

}