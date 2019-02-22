package com.ragdroid.mvi.main

import com.ragdroid.data.entity.CharacterMarvel
import com.ragdroid.mvi.R
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.models.CharacterItemState
import com.ragdroid.mvvmi.core.MviAction
import com.ragdroid.mvvmi.core.MviResult
import com.ragdroid.mvvmi.core.MviState

/**
 * State of the MainView
 * Created by garimajain on 22/11/17.
 */
data class MainViewState(
        val loading: Boolean,
        val characters: List<CharacterItemState>,
        val loadingError: Throwable?,
        val pullToRefreshing: Boolean,
        val pullToRefreshError: Throwable?): MviState() {

    companion object Factory {
        fun init() = MainViewState(
                loading = true,
                characters = emptyList(),
                loadingError = null,
                pullToRefreshing = false,
                pullToRefreshError = null
        )
    }

    fun reduce(result: MainResult, resources: ResourceProvider): MainViewState {

        val characters = characters
        return when (result) {
            is MainResult.Loading -> copy(
                    loading = true,
                    loadingError = null)
            is MainResult.LoadingError -> copy(
                    loading = false,
                    loadingError = result.throwable)

            is MainResult.LoadingComplete -> {
                val characterStates = reduceCharactersList(characters, result.characters, resources)
                copy(
                        loading = false,
                        loadingError = null,
                        characters = characterStates)
            }

            is MainResult.PullToRefreshing -> copy(
                    loading = false,
                    pullToRefreshing = true,
                    pullToRefreshError = null)
            is MainResult.PullToRefreshError -> copy(
                    pullToRefreshing = false,
                    pullToRefreshError = result.throwable)
            is MainResult.PullToRefreshComplete -> copy(
                    pullToRefreshing = false,
                    pullToRefreshError = null,
                    characters = reduceCharactersList(characters, result.characters, resources))

            is MainResult.DescriptionResult -> {
                val previousItemState = findItemWithId(result.characterId)
                val previousItemStateIndex = characters.indexOf(previousItemState)

                val newItemState = previousItemState.reduce(resources, result)

                val newCharactersList = characters.slice(0 until previousItemStateIndex)
                        .plus(listOf(newItemState))
                        .plus(characters.slice(previousItemStateIndex + 1 until characters.size))
                copy(characters = newCharactersList)
            }

        }
    }


    private fun findItemWithId(characterId: Long): CharacterItemState =
            characters.find { it.characterId == characterId }!!



    private fun reduceCharactersList(previousStateList: List<CharacterItemState>,
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

sealed class MainAction: MviAction() {
    object PullToRefresh: MainAction()
    object LoadData: MainAction()
    data class LoadDescription(val characterId: Long): MainAction()
}

sealed class MainResult: MviResult() {

    object Loading: MainResult()
    data class LoadingError(val throwable: Throwable): MainResult()
    data class LoadingComplete(val characters: List<CharacterMarvel>): MainResult()

    object PullToRefreshing: MainResult()
    data class PullToRefreshError(val throwable: Throwable): MainResult()
    data class PullToRefreshComplete(val characters: List<CharacterMarvel>): MainResult()

    sealed class DescriptionResult(val characterId: Long) : MainResult() {
        data class DescriptionLoading(private val id: Long) : DescriptionResult(id)
        data class DescriptionError(private val id: Long, val throwable: Throwable): DescriptionResult(id)
        data class DescriptionLoadComplete(private val id: Long, val description: String): DescriptionResult(id)
    }
}