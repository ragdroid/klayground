package com.ragdroid.mvi.models

import com.ragdroid.data.entity.CharacterMarvel

/**
 * State of the MainView
 * Created by garimajain on 22/11/17.
 */
data class MainViewState(
        val loading: Boolean,
        val characters: List<CharacterMarvel>,
        val loadingError: Throwable?,
        val pullToRefreshing: Boolean,
        val pullToRefreshError: Throwable?){

    companion object Factory {
        fun init() = MainViewState(
                loading = false,
                characters = emptyList(),
                loadingError = null,
                pullToRefreshing = false,
                pullToRefreshError = null
        )
    }
}


sealed class MainResults {
    object Loading: MainResults()
    data class LoadingError(val throwable: Throwable): MainResults()
    data class LoadingComplete(val characters: List<CharacterMarvel>): MainResults()
    object PullToRefreshing: MainResults()
    data class PullToRefreshError(val throwable: Throwable): MainResults()
    data class PullToRefreshComplete(val characters: List<CharacterMarvel>): MainResults()
}