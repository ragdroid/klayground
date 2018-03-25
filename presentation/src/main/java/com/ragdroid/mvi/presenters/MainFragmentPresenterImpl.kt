package com.ragdroid.mvi.presenters

import com.ragdroid.data.MainRepository
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.data.entity.CharacterMarvel
import com.ragdroid.mvi.R
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainFragmentPresenter
import com.ragdroid.mvi.main.MainFragmentView
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import com.ragdroid.mvi.models.CharacterItemState
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
                    private val schedulerProvider: SchedulerProvider,
                    private val resources: ResourceProvider) : MainFragmentPresenter {

    val subject: PublishSubject<MainResult> = PublishSubject.create()
    var disposable: Disposable? = null

    var view: MainFragmentView? = null

    override fun attachView(view: MainFragmentView) {
        this.view = view
        val loadingPartialResult = view.loadingIntent()
                .observeOn(schedulerProvider.io())
                .flatMap { ignored -> repository.fetchCharacters().toObservable() }
                .map { states -> MainResult.LoadingComplete(states) as MainResult }
                .startWith (MainResult.Loading)
                .onErrorReturn { error -> MainResult.LoadingError(error) }

        val pullToRefreshPartialResult = view.pullToRefreshIntent()
                .observeOn(schedulerProvider.io())
                .flatMap { ignored -> repository.fetchCharacters().toObservable() }
                .map { items -> MainResult.PullToRefreshComplete(items) as MainResult }
                .startWith(MainResult.PullToRefreshing)
                .onErrorReturn { error -> MainResult.PullToRefreshError(error) }

        val allIntentObservable = Observable.merge(loadingPartialResult, pullToRefreshPartialResult)
        val initialState = MainViewState.init()

        allIntentObservable.subscribe(subject)

        disposable = subject
                .scan(initialState) { state, result -> reducer(state, result)}
                .observeOn(schedulerProvider.ui())
                .subscribe (
                        { state ->
                            Timber.d("Got state $state")
                            view.render(state) },
                        {e -> Timber.e(e)})


    }

    private fun initialState(result: CharacterMarvel) =
            CharacterItemState.init(result.id, result.name, result.imageUrl, resources.getString(R.string.description))


    private fun reducer(previousState: MainViewState, result: MainResult): MainViewState =
            when (result) {
                is MainResult.Loading -> previousState.copy(
                        loading = true,
                        loadingError = null)
                is MainResult.LoadingError -> previousState.copy(
                        loading = false,
                        loadingError = result.throwable)
                is MainResult.LoadingComplete -> {
                    val characterStates = reducer(previousState.characters, result.characters)
                    previousState.copy(
                            loading = false,
                            loadingError = null,
                            characters = characterStates)
                }

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
                        characters = reducer(previousState.characters, result.characters))

                is MainResult.DescriptionResult ->
                    previousState.copy(
                            characters =
                    )
                    reducer(previousState.findItemWithId(result.characterId), result)

            }

    private fun reducer(previousState: CharacterItemState?, result: MainResult.DescriptionResult): CharacterItemState {
        when(result) {
            is MainResult.DescriptionResult.DescriptionLoading ->
            is MainResult.DescriptionResult.DescriptionError -> TODO()
            is MainResult.DescriptionResult.DescriptionLoadComplete -> TODO()
        }
    }

    private fun reducer(previousStateList: List<CharacterItemState>, resultList: List<CharacterMarvel>): List<CharacterItemState> {
        return resultList
                .map {
                    initialState(it) //TODO for now whenever list loads, we lose description
                }
    }

    override fun detachView() {
        disposable?.dispose()
        disposable = null
        view = null
    }

}

private fun MainViewState.findItemWithId(characterId: Long): CharacterItemState? =
        characters.find { it.characterId == characterId }

