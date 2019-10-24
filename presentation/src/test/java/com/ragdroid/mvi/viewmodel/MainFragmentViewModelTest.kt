package com.ragdroid.mvi.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.TestDataFactory
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainAction
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit


class MainFragmentViewModelTest: BaseUnitTest() {

    val resourceProvider: ResourceProvider = mock() {
        on { getString(any()) } doReturn "Description"
    }

    val mainRepository: MainRepository = mock() {
        on { fetchCharactersSingle() } doReturn Single.just(TestDataFactory.mockCharacters)
        on { fetchCharacterSingle(1234) } doReturn Single.just(TestDataFactory.marcelCharacter1)
    }

    private lateinit var viewmodel: MainFragmentViewModel


    @Before
    fun setUp() {
        viewmodel = MainFragmentViewModel(resourceProvider, mainRepository)
    }

    @Test
    fun testLoadData() {

        val subscriber = viewmodel.stateFlowable().test()

        viewmodel.onAction(MainAction.LoadData)

        testScheduler.triggerActions()
        testScheduler.advanceTimeBy(3000, TimeUnit.MILLISECONDS)

        subscriber.assertNoErrors()
        subscriber.values()
                .forEach {
                    println(it)
                }
        subscriber.assertValueAt(subscriber.values().size - 1) {
            it.characters.isNotEmpty()
        }

    }
}