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
        val pullToRefreshError: Throwable?,
        val emptyStateVisible: Boolean){

    companion object Factory {
        fun init() = MainViewState(
                loading = true,
                characters = emptyList(),
                loadingError = null,
                pullToRefreshing = false,
                pullToRefreshError = null,
                emptyStateVisible = false
        )
    }
}


sealed class MainResult {
    object Loading: MainResult()
    object LoadingEmpty: MainResult()
    data class LoadingError(val throwable: Throwable): MainResult()
    data class LoadingComplete(val characters: List<CharacterMarvel>): MainResult()
    object PullToRefreshing: MainResult()
    object PullToRefreshEmpty: MainResult()
    data class PullToRefreshError(val throwable: Throwable): MainResult()
    data class PullToRefreshComplete(val characters: List<CharacterMarvel>): MainResult()
}