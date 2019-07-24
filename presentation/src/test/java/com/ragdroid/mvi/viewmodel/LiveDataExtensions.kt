package com.ragdroid.mvi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * https://stackoverflow.com/questions/44270688/unit-testing-room-and-livedata
 */
fun <T> LiveData<T>.blockingObserve(invocationIndex: Int = 1): T? {
    var value: T? = null
    val latch = CountDownLatch(invocationIndex)
    val innerObserver = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(innerObserver)
    latch.await(2, TimeUnit.SECONDS)
    return value
}