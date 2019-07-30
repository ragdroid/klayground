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
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

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
        viewmodel = CharactersViewModel(mainRepository, resourceProvider, testDispatcherProvider)
    }

    @Test
    fun testLoadData() = runBlocking {

        val loadDataFlow = flow { emit(MainAction.LoadData) }

        viewmodel.processActions(loadDataFlow)

        val state = viewmodel.stateLiveData().blockingObserve()

        println(state)

        assertTrue(state!!.characters.isNotEmpty())

    }
}