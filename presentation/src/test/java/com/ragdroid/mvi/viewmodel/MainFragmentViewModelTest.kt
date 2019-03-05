package com.ragdroid.mvi.viewmodel

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.ragdroid.data.MainRepository
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.mvi.TestDataFactory
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainAction
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit


class MainFragmentViewModelTest {

    val resourceProvider: ResourceProvider = mock() {
        on { getString(any()) } doReturn "Description"
    }
    val testScheduler = TestScheduler()
    val schedulerProvider: SchedulerProvider = mock {
        on { computation() } doReturn testScheduler
        on { io() } doReturn testScheduler
        on { ui() } doReturn testScheduler
    }
    val mainRepository: MainRepository = mock() {
        on { fetchCharacters() } doReturn Single.just(TestDataFactory.mockCharacters)
        on { fetchCharacter(1234) } doReturn Single.just(TestDataFactory.marcelCharacter1)
    }

    private lateinit var viewmodel: MainFragmentViewModel


    @Before
    fun setUp() {
        viewmodel = MainFragmentViewModel(resourceProvider, schedulerProvider, mainRepository)
    }

    @Test
    fun testLoadData() {

        viewmodel.processActions(Flowable.just(MainAction.LoadData))

        testScheduler.triggerActions()
        testScheduler.advanceTimeBy(3000, TimeUnit.MILLISECONDS)

        val subscriber = viewmodel.stateFlowable().test()
        subscriber.assertNoErrors()
        subscriber.values()
                .forEach {
                    println(it)
                }
        subscriber.assertValue {
            it.characters.isNotEmpty()
        }

    }
}