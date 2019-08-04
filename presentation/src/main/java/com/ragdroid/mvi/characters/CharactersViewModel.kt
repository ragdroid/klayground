package com.ragdroid.mvi.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.helpers.mergeWith
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainNavigation
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CharactersViewModel @Inject constructor(
        private val mainRepository: MainRepository,
        private val resourceProvider: ResourceProvider
): ViewModel() {

    fun onAction(action: MainAction) = broadcastChannel.offer(action)

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
                    .flatMapMerge { actionToResultFlow(it) }
                    .onEach {
                        Timber.v("onResult $it")
                    }
                    .scan(MainViewState.init()) { state, result: MainResult -> reduce(state, result) }
                    .onEach {
                        Timber.v("onState $it")
                        stateLiveData.postValue(it)
                    }
                    .onStart { Timber.d("subscribed to states") }
                    .collect {
                        Timber.v("onState $it")
                        stateLiveData.postValue(it)
                    }
        }
    }

    private fun reduce(state: MainViewState, result: MainResult): MainViewState {
        return state.reduce(result, resourceProvider)
    }

    private fun actionToResultFlow(action: MainAction): Flow<MainResult> {
        return when(action) {
            is MainAction.PullToRefresh -> flow {
                emit(MainResult.PullToRefreshing)
                val characters = mainRepository.fetchCharacters()
                emit(MainResult.PullToRefreshComplete(characters))
            }.catch { exception ->
                Timber.e(exception)
                navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                emit(MainResult.PullToRefreshError(exception))
            }
            is MainAction.LoadData -> flow {
                emit(MainResult.Loading)
                val characters = mainRepository.fetchCharacters()
                emit(MainResult.LoadingComplete(characters))
            }
//                    .delayEach(1000)
                    .catch { exception ->
                Timber.e(exception)
                navigate(MainNavigation.Snackbar(exception.message ?: "Unknown Error"))
                emit(MainResult.LoadingError(exception))
            }
            is MainAction.LoadDescription -> flow {
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

}