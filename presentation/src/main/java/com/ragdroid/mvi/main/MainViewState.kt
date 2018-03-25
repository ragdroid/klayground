package com.ragdroid.mvi.main

import com.ragdroid.data.entity.CharacterMarvel
import com.ragdroid.mvi.models.CharacterItemState

/**
 * State of the MainView
 * Created by garimajain on 22/11/17.
 */
data class MainViewState(
        val loading: Boolean,
        val characters: List<CharacterItemState>,
        val loadingError: Throwable?,
        val pullToRefreshing: Boolean,
        val pullToRefreshError: Throwable?){

    companion object Factory {
        fun init() = MainViewState(
                loading = true,
                characters = emptyList(),
                loadingError = null,
                pullToRefreshing = false,
                pullToRefreshError = null
        )
    }
}

sealed class MainAction {
    object PullToRefresh: MainAction()
    object LoadData: MainAction()
    data class LoadDescription(val characterId: Long): MainAction()
}

sealed class MainResult {

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