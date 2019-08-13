package com.ragdroid.mvi.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.helpers.DispatchProvider
import com.ragdroid.mvi.helpers.merge
import com.ragdroid.mvi.helpers.mergeWith
import com.ragdroid.mvi.helpers.ofType
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainNavigation
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import hu.akarnokd.kotlin.flow.publish
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx2.await
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class CharactersViewModel @Inject constructor(
        private val mainRepository: MainRepository,
        private val resourceProvider: ResourceProvider,
        private val dispatchProvider: DispatchProvider
): ViewModel() {

    fun onAction(action: MainAction) = broadcastChannel.offer(action)

    //we can also use kotlin-flow-extensions `PublishSubject` here
    var broadcastChannel = ConflatedBroadcastChannel<MainAction>()
    var actionsFlow = broadcastChannel.openSubscription().consumeAsFlow()

    fun stateLiveData(): LiveData<MainViewState> = stateLiveData
    private val stateLiveData = MutableLiveData<MainViewState>()

    fun navigationLiveData(): LiveData<MainNavigation> = navigationLiveData
    private val navigationLiveData = MutableLiveData<MainNavigation>()

    fun navigate(navigationState: MainNavigation) = navigationLiveData.postValue(navigationState)

    fun processActions(actions: Flow<MainAction>) {
        viewModelScope.launch {
            actionsFlow.mergeWith(actions)
                    .onEach {
                        Timber.v("onAction $it")
                    }
                    //to demonstrate streams
//                    .publish {
//                        actionToResultTransformer(it)
//                    }
                    //to demonstrate non-streams solution
                    .flatMapMerge {
                        actionToResultFlow(it)
                    }
                    .onEach {
                        Timber.v("onResult $it")
                    }
                    .scan(MainViewState.init()) { state, result: MainResult -> reduce(state, result) }
                    .onStart { Timber.d("subscribed to states") }
                    .collect {
                        Timber.v("onState $it")
                        stateLiveData.postValue(it)
                    }
        }
    }

    private fun actionToResultFlow(action: MainAction): Flow<MainResult> = flow {
        when(action) {
            is MainAction.PullToRefresh -> {
                try {
                    emit(MainResult.PullToRefreshing)
                    val characters = mainRepository.fetchCharactersSingle().await()
                    emit(MainResult.PullToRefreshComplete(characters))
                } catch (exception: Exception) {
                    Timber.e(exception)
                    navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                    emit(MainResult.PullToRefreshError)
                }
            }
            is MainAction.LoadData -> {
                try {
                    emit(MainResult.Loading)
                    val characters = mainRepository.fetchCharacters()
                    emit(MainResult.LoadingComplete(characters))
                } catch (exception: Exception) {
                    Timber.e(exception)
                    navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                    emit(MainResult.LoadingError)
                }
            }
            is MainAction.LoadDescription -> {
                try {
                    emit(MainResult.DescriptionResult.DescriptionLoading(action.characterId))
                    val character = mainRepository.fetchCharacter(action.characterId)
                    delay(2000L)
                    emit(MainResult.DescriptionResult.DescriptionLoadComplete(character.id, character.description))
                } catch (exception : Exception) {
                    navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                    Timber.e(exception)
                    emit(MainResult.DescriptionResult.DescriptionError(action.characterId, exception))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
            .catch { exception ->
                Timber.e(exception)
                navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                emit(MainResult.LoadingError)
            }

    private fun actionToResultTransformer(actionsFlow: Flow<MainAction>): Flow<MainResult> {
        return loadingResult(actionsFlow.ofType(MainAction.LoadData::class.java))
                .merge(loadDescriptionResult(actionsFlow.ofType(MainAction.LoadDescription::class.java)),
                        pullToRefreshResult(actionsFlow.ofType(MainAction.PullToRefresh::class.java)))
    }

    private fun loadDescriptionResult(actionsFlow: Flow<MainAction.LoadDescription>): Flow<MainResult>  = flow {
        actionsFlow.flatMapMerge { action ->
            flow {
                emit(mainRepository.fetchCharacter(action.characterId)) }
                    .delayEach(2000)
                    .map { item ->
                        MainResult.DescriptionResult.DescriptionLoadComplete(item.id, item.description) as MainResult
                    }
                    .onStart { emit(MainResult.DescriptionResult.DescriptionLoading(action.characterId)) }
                    .catch {
                        navigate(MainNavigation.Snackbar(it.message ?: "Unknown Error"))
                        Timber.e(it)
                        emit(MainResult.DescriptionResult.DescriptionError(action.characterId, it))
                    }
        }
    }

    private fun pullToRefreshResult(actionsFlow: Flow<MainAction>): Flow<MainResult> = flow {
        actionsFlow.flatMapMerge {
            flow {
                emit(MainResult.PullToRefreshing)
                //This is done only to demonstrate RxJava-Coroutine interop
                //We should use available coroutine API instead :
                //val characters = mainRepository.fetchCharacters()
                val characters = mainRepository.fetchCharactersSingle().await()
                emit(MainResult.PullToRefreshComplete(characters))
            }
                    .flowOn(dispatchProvider.io())
                    .catch { exception ->
                        Timber.e(exception)
                        navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                        emit(MainResult.PullToRefreshError)
                    }
        }
    }

    private fun loadingResult(actionsFlow: Flow<MainAction.LoadData>): Flow<MainResult> = flow {
        actionsFlow.flatMapMerge {
            flow {
                emit(MainResult.Loading)
                val characters = mainRepository.fetchCharacters()
                emit(MainResult.LoadingComplete(characters))
            }
                    //uncommeent this to see unit test behavior with delays
                    .delayEach(1000)
                    .catch { exception ->
                        Timber.e(exception)
                        navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                        emit(MainResult.LoadingError)
                    }
        }

    }


    private fun reduce(state: MainViewState, result: MainResult): MainViewState {
        return state.reduce(result, resourceProvider)
    }
}