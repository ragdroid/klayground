package com.ragdroid.mvi.viewmodel

import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainNavigation
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import com.ragdroid.mvvmi.core.MviViewModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.rxFlowable
import kotlinx.coroutines.rx2.rxSingle
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(private val resourceProvider: ResourceProvider,
                                                private val mainRepository: MainRepository):
        MviViewModel<MainAction, MainResult, MainViewState>(MainViewState.init()) {

    override fun actionsToResultTransformer(actions: Flowable<MainAction>): Flowable<MainResult> =
            actions.publish { shared ->
                Flowable.merge(loadingResult(shared.ofType(MainAction.LoadData::class.java)),
                        loadDescriptionResult(shared.ofType(MainAction.LoadDescription::class.java)),
                        pullToRefreshResult(shared.ofType(MainAction.PullToRefresh::class.java)))

            }

    override fun reduce(previousState: MainViewState, result: MainResult): MainViewState {
        return previousState.reduce(result, resourceProvider)
    }


    private fun loadDescriptionResult(loadDescriptionActionStream: Flowable<MainAction.LoadDescription>): Flowable<MainResult> {
        return loadDescriptionActionStream
                .observeOn(Schedulers.io())
                .flatMap { action ->

                    mainRepository.fetchCharacterSingle(action.characterId).toFlowable()
                            .delay(2000, TimeUnit.MILLISECONDS, Schedulers.computation())
                            .map { item ->
                                MainResult.DescriptionResult.DescriptionLoadComplete(item.id, item.description) as MainResult
                            }
                            .startWith(MainResult.DescriptionResult.DescriptionLoading(action.characterId))
                            .onErrorReturn { error ->
                                Timber.e(error)
                                navigate(MainNavigation.Snackbar(error.message ?: "Unknown Error"))
                                MainResult.DescriptionResult.DescriptionError(action.characterId, error)
                            }
                }
    }

    private fun pullToRefreshResult(pullToRefreshActionStream: Flowable<MainAction.PullToRefresh>): Flowable<MainResult> {
        return pullToRefreshActionStream
                .observeOn(Schedulers.io())
                .flatMap { ignored ->
                    //This is done only to demonstrate RxJava-Coroutine interop
                    //We should use available Rx API instead :
                    //val characters = mainRepository.fetchCharactersSingle()
                    rxSingle {
                        mainRepository.fetchCharacters()
                    }.toFlowable()
                            .map { items -> MainResult.PullToRefreshComplete(items) as MainResult }
                            .startWith(MainResult.PullToRefreshing)
                            .onErrorReturn { error ->
                                navigate(MainNavigation.Snackbar(error.message ?: "Unknown Error"))
                                MainResult.PullToRefreshError
                            }
                }
    }

    private fun loadingResult(loadDataActionStream: Flowable<MainAction.LoadData>)
            : Flowable<MainResult> {
        return loadDataActionStream
                .observeOn(Schedulers.io())
                .flatMap { ignored ->
                    mainRepository.fetchCharactersSingle().toFlowable()
                            .map { states -> MainResult.LoadingComplete(states) as MainResult }
                            .startWith(MainResult.Loading)
                            .onErrorReturn {
                                error ->
                                navigate(MainNavigation.Snackbar(error.message ?: "Unknown Error"))
                                MainResult.LoadingError
                            }
                }
    }


}