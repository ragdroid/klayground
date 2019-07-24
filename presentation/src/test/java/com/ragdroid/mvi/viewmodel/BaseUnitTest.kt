package com.ragdroid.mvi.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule

open class BaseUnitTest {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()


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
