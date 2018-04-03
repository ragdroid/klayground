package com.ragdroid.mvi.main

import com.ragdroid.data.entity.Item

/**
 * State of the MainView
 * Created by garimajain on 22/11/17.
 */
data class State(
        val loading: Boolean,
        val items: List<Item>,
        val loadingError: Throwable?,
        val pullToRefreshing: Boolean,
        val pullToRefreshError: Throwable?,
        val emptyStateVisible: Boolean){

    companion object Factory {
        fun init() = State(
                loading = true,
                items = emptyList(),
                loadingError = null,
                pullToRefreshing = false,
                pullToRefreshError = null,
                emptyStateVisible = false
        )
    }
}


sealed class Result {
    object Loading: Result()
    object LoadingEmpty: Result()
    data class LoadingError(val throwable: Throwable): Result()
    data class LoadingComplete(val characters: List<Item>): Result()

    object PullToRefreshing: Result()
    object PullToRefreshEmpty: Result()
    data class PullToRefreshError(val throwable: Throwable): Result()
    data class PullToRefreshComplete(val characters: List<Item>): Result()
}