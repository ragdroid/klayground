package com.ragdroid.mvi.main

import com.ragdroid.data.entity.CharacterMarvel
import com.ragdroid.mvi.R
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.models.CharacterItemState
import com.ragdroid.mvvmi.core.MviAction
import com.ragdroid.mvvmi.core.MviResult
import com.ragdroid.mvvmi.core.MviState
import com.ragdroid.mvvmi.core.NavigationState

data class MainViewState(val characters: List<CharacterItemState>,
                         val emptyState: EmptyState,
                         val loadingState: LoadingState): MviState {

    sealed class EmptyState {
        object None: EmptyState()
        object NoData: EmptyState()
        object NoInternet: EmptyState()
    }

    sealed class LoadingState {
        object None: LoadingState()
        object Loading: LoadingState()
        object PullToRefreshing: LoadingState()
    }

    companion object Factory {
        fun init() = MainViewState(emptyList(), EmptyState.NoData, LoadingState.None)
    }

    fun reduce(result: MainResult, resources: ResourceProvider): MainViewState {

        return when (result) {
            is MainResult.Loading -> copy(loadingState = LoadingState.Loading)
            is MainResult.LoadingError -> copy(loadingState = LoadingState.None,
                    emptyState = EmptyState.NoData)

            is MainResult.LoadingComplete -> {
                val characterStates = reduceCharactersList(null, result.characters, resources)
                copy(characterStates, loadingState = LoadingState.None,
                        emptyState = EmptyState.None)
            }

            is MainResult.PullToRefreshing -> {
                copy(loadingState = LoadingState.PullToRefreshing)
            }
            is MainResult.PullToRefreshError -> copy(loadingState = LoadingState.None)
            is MainResult.PullToRefreshComplete ->
                copy(characters = reduceCharactersList(characters, result.characters, resources),
                        loadingState = LoadingState.None,
                        emptyState = EmptyState.None)

            is MainResult.DescriptionResult -> {
                val previousCharacters = characters
                val previousItemState = findItemWithId(result.characterId)
                val previousItemStateIndex = previousCharacters.indexOf(previousItemState)

                val newItemState = previousItemState.reduce(resources, result)

                val newCharactersList = previousCharacters.slice(0 until previousItemStateIndex)
                        .plus(listOf(newItemState))
                        .plus(previousCharacters.slice(previousItemStateIndex + 1 until previousCharacters.size))
                copy(characters = newCharactersList)
            }
        }
    }


    private fun findItemWithId(characterId: Long): CharacterItemState =
            characters.find { it.characterId == characterId }!!


    private fun reduceCharactersList(previousStateList: List<CharacterItemState>?,
                                     resultList: List<CharacterMarvel>,
                                     resources: ResourceProvider): List<CharacterItemState> {
        fun initialState(result: CharacterMarvel) =
                CharacterItemState.init(result.id, result.name, result.imageUrl, resources.getString(R.string.description))
        return resultList
                .map {
                    initialState(it) //TODO for now whenever list loads, we lose description
                }
    }

}


sealed class MainAction: MviAction {
    object PullToRefresh: MainAction()
    object LoadData: MainAction()
    data class LoadDescription(val characterId: Long): MainAction()
}

sealed class MainResult: MviResult {

    object Loading: MainResult()
    object LoadingError: MainResult()
    data class LoadingComplete(val characters: List<CharacterMarvel>): MainResult()

    object PullToRefreshing: MainResult()
    object PullToRefreshError: MainResult()
    data class PullToRefreshComplete(val characters: List<CharacterMarvel>): MainResult()

    sealed class DescriptionResult(val characterId: Long) : MainResult() {
        data class DescriptionLoading(private val id: Long) : DescriptionResult(id)
        data class DescriptionError(private val id: Long,
                                    val throwable: Throwable): DescriptionResult(id)
        data class DescriptionLoadComplete(private val id: Long,
                                           val description: String): DescriptionResult(id)
    }
}

sealed class MainNavigation: NavigationState {
    data class Snackbar(val message: String): MainNavigation()
}