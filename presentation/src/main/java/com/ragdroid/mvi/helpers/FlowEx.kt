package com.ragdroid.mvi.helpers

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun <T> Flow<T>.mergeWith(other: Flow<T>): Flow<T> = channelFlow {
    // collect from one coroutine and send it
    launch {
        collect { send(it) }
    }
    // collect and send from this coroutine, too, concurrently
    other.collect { send(it) }
}

fun <T> Flow<T>.merge(other: Flow<T>,
                      other2: Flow<T>): Flow<T> = channelFlow {
    // collect from one coroutine and send it
    launch {
        collect { send(it) }
    }
    // collect and send from this coroutine, too, concurrently
    launch {
        other.collect {
            send(it)
        }
    }
    launch {
        other2.collect {
            send(it)
        }
    }
}

fun <T> Flow<T>.merge3(other: Flow<T>,
                      other2: Flow<T>,
                      other3: Flow<T>): Flow<T> = channelFlow {
    // collect from one coroutine and send it
    launch {
        collect { send(it) }
    }
    // collect and send from this coroutine, too, concurrently
    launch {
        other.collect {
            send(it)
        }
    }
    launch {
        other2.collect {
            send(it)
        }
    }
    launch {
        other3.collect {
            send(it)
        }
    }
}

fun <U, V> Flow<U>.ofType(clazz: Class<V>): Flow<V> {
    return filter {
        clazz.isInstance(it)
    }.cast(clazz)
}

fun <U, V> Flow<U>.cast(clazz: Class<V>): Flow<V> {
    return map {
        clazz.cast(it)
    }
}


