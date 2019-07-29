package com.ragdroid.mvi.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.mergeWith(other: Flow<T>): Flow<T> = channelFlow {
    // collect from one coroutine and send it
    launch {
        collect { send(it) }
    }
    // collect and send from this coroutine, too, concurrently
    other.collect { send(it) }
}

fun <T> Flow<T>.merge(vararg other: Flow<T>): Flow<T> = channelFlow {
    // collect from one coroutine and send it
    launch {
        collect { send(it) }
    }
    // collect and send from this coroutine, too, concurrently
    other.forEach {
        it.collect {
            send(it)
        }
    }
}

