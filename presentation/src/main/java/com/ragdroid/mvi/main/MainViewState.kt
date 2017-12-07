package com.ragdroid.mvi.main

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


sealed class MainResult {
    object Loading: MainResult()
    data class LoadingError(val throwable: Throwable): MainResult()
    data class LoadingComplete(val characters: List<CharacterMarvel>): MainResult()
    object PullToRefreshing: MainResult()
    data class PullToRefreshError(val throwable: Throwable): MainResult()
    data class PullToRefreshComplete(val characters: List<CharacterMarvel>): MainResult()
}