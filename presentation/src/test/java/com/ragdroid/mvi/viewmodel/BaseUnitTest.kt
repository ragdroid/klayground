package com.ragdroid.mvi.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ragdroid.mvi.helpers.DispatchProvider
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule

open class BaseUnitTest {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()


    var testDispatcher = TestCoroutineDispatcher()

    var testDispatcherProvider = object: DispatchProvider {

        override fun main(): CoroutineDispatcher = testDispatcher

        override fun io(): CoroutineDispatcher = testDispatcher

        override fun computation(): CoroutineDispatcher = testDispatcher

    }

    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }


    companion object {
        @JvmStatic val testScheduler = TestScheduler()

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { testScheduler}
            RxJavaPlugins.setInitComputationSchedulerHandler { testScheduler}
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            RxAndroidPlugins.reset()
        }
    }
}
