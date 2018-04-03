package com.ragdroid.mvi.presenters

import com.ragdroid.data.Repository
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.mvi.main.Presenter
import com.ragdroid.mvi.main.View
import com.ragdroid.mvi.main.Result
import com.ragdroid.mvi.main.State
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by garimajain on 19/11/17.
 */
class PresenterImpl
@Inject constructor(private val repository: Repository,
                    private val schedulerProvider: SchedulerProvider) : Presenter {

    var disposable: Disposable? = null

    var view: View? = null

    override fun attachView(view: View) {
        this.view = view

        val loadingResult = subscribeToLoading(view)

        val pullToRefreshResult = subscribeToPullToRefresh(view)

        val allResultObservable = Observable.merge(loadingResult, pullToRefreshResult)


        disposable = subscribeToStates(allResultObservable)


    }

    private fun subscribeToStates(allResultObservable: Observable<Result>): Disposable? {
        return allResultObservable
                .scan(State.init()) {
                    state, result -> reducer(state, result)
                }
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        { state ->
                            Timber.d("Got state $state")
                            view.render(state)
                        },
                        { Timber.e(it) })
    }

    private fun subscribeToPullToRefresh(view: View): Observable<Result> {
        return view.pullToRefreshIntent()
                .flatMap {
                    repository.loadItems().toObservable()
                            .subscribeOn(schedulerProvider.io())
                            .map { items ->
                                if (items.isEmpty())
                                    Result.PullToRefreshEmpty
                                else
                                    Result.PullToRefreshComplete(items)
                            }
                            .startWith(Result.PullToRefreshing)
                }
                .onErrorReturn { error -> Result.PullToRefreshError(error) }
    }

    private fun subscribeToLoading(view: View): Observable<Result> {
        return view.loadingIntent()
                .flatMap {
                    repository.loadItems().toObservable()
                            .subscribeOn(schedulerProvider.io())
                            .map { items ->
                                if (items.isEmpty())
                                    Result.LoadingEmpty
                                else
                                    Result.LoadingComplete(items)
                            }
                }
                .onErrorReturn { error -> Result.LoadingError(error) }
    }


    private fun reducer(previousState: State, result: Result): State =
            when (result) {
                is Result.Loading -> previousState.copy(
                        loading = true,
                        loadingError = null)
                is Result.LoadingError -> previousState.copy(
                        loading = false,
                        loadingError = result.throwable)
                is Result.LoadingComplete -> previousState.copy(
                        loading = false,
                        loadingError = null,
                        items = result.characters)
                is Result.LoadingEmpty -> previousState.copy(
                        loading = false,
                        loadingError = null,
                        items = emptyList(),
                        emptyStateVisible = true)
                is Result.PullToRefreshing -> previousState.copy(
                        loading = false,
                        pullToRefreshing = true,
                        pullToRefreshError = null)
                is Result.PullToRefreshError -> previousState.copy(
                        pullToRefreshing = false,
                        pullToRefreshError = result.throwable)
                is Result.PullToRefreshComplete -> previousState.copy(
                        pullToRefreshing = false,
                        pullToRefreshError = null,
                        items = result.characters)
                is Result.PullToRefreshEmpty -> previousState.copy(
                        pullToRefreshing = false,
                        loadingError = null,
                        items = emptyList(),
                        emptyStateVisible = true
                )
            }

    override fun detachView() {
        disposable?.dispose()
        disposable = null
        view = null
    }

}