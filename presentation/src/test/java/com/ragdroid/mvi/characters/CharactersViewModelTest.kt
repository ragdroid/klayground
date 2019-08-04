package com.ragdroid.mvi.characters

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.TestDataFactory
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.viewmodel.BaseUnitTest
import com.ragdroid.mvi.viewmodel.blockingObserve
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CharactersViewModelTest: BaseUnitTest() {

    val resourceProvider: ResourceProvider = mock() {
        on { getString(any()) } doReturn "Description"
    }

    val mainRepository: MainRepository = mock() {
        onBlocking { fetchCharacters() } doReturn TestDataFactory.mockCharacters
        onBlocking { fetchCharacter(1234) } doReturn TestDataFactory.marcelCharacter1
    }



    private lateinit var viewmodel: CharactersViewModel


    @Before
    fun setUp() {
        super.setup()
        viewmodel = CharactersViewModel(mainRepository, resourceProvider)
    }

    @Test
    fun testLoadData() = runBlocking {

        val loadDataFlow = flow { emit(MainAction.LoadData) }

        viewmodel.processActions(loadDataFlow)

//        testDispatcher.advanceTimeBy(2000)

        val state = viewmodel.stateLiveData().blockingObserve()

        println(state)

        assertTrue(state!!.characters.isNotEmpty())

    }
}