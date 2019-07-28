package com.ragdroid.mvi.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainResult
import com.ragdroid.mvi.main.MainViewState
import hu.akarnokd.kotlin.flow.concatWith
import hu.akarnokd.kotlin.flow.publish
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
        private val mainRepository: MainRepository,
        private val resourceProvider: ResourceProvider
): ViewModel() {

    var broadcastChannel = ConflatedBroadcastChannel<MainAction>()
    var actionsFlow = broadcastChannel.openSubscription().consumeAsFlow()

    fun stateLiveData(): LiveData<MainViewState> = stateLiveData
    private val stateLiveData = MutableLiveData<MainViewState>()

    fun processActions(actions: Flow<MainAction>) {
        viewModelScope.launch {
            actionsFlow.concatWith(actions)
                    .onEach {
                        Timber.v("onAction $it")
                    }
                    .flatMapMerge { actionToResultFlow(it) }
                    .onEach {
                        Timber.v("onResult $it")
                    }
                    .scan(MainViewState.init()) { state, result: MainResult -> reduce(state, result) }
                    .onEach {
                        Timber.v(it.toString())
                        stateLiveData.postValue(it)
                    }
                    .onStart { Timber.d("subscribed to states") }
                    .collect {
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
               try {
                   val characters = mainRepository.fetchCharacters()
                   emit(MainResult.PullToRefreshComplete(characters))
               } catch (exception: Exception) {
                   emit(MainResult.PullToRefreshError(exception))
               }
           }
           is MainAction.LoadData -> flow {
               emit(MainResult.Loading)
               try {
                   val characters = mainRepository.fetchCharacters()
                   emit(MainResult.LoadingComplete(characters))
               } catch (exception: Exception) {
                   emit(MainResult.LoadingError(exception))
               }
           }
           else -> flow {  }
       }
    }

}