package com.ragdroid.data

import com.ragdroid.api.MarvelApi
import com.ragdroid.data.base.Helpers
import com.ragdroid.data.entity.AppConfig
import com.ragdroid.data.entity.CharacterMapper
import com.ragdroid.data.entity.CharacterMarvel
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.*

/**
 * Created by garimajain on 09/12/17.
 */

object MainRepositorySpec: Spek({
    given("a repository") {
        var mockApi = mock(MarvelApi::class)
        var mapper = mock(CharacterMapper::class)
        var config = mock(AppConfig::class)
        var helpers = mock(Helpers::class)
        var repository = MainRepositoryImpl(mockApi, mapper, config, helpers)

        beforeEachTest {
            mockApi = mock(MarvelApi::class)
            mapper = mock(CharacterMapper::class)
            config = mock(AppConfig::class)
            helpers = mock(Helpers::class)

            `when`(config.publicKey).thenReturn("publicKEY")
            `when`(config.privateKey).thenReturn("privateKey")
            `when`(mapper.map(any())).thenReturn(getFakeMarvelCharacterData())
            `when`(helpers.buildMD5Digest(anyString())).thenReturn("Digest")

            repository = MainRepositoryImpl(mockApi, mapper, config, helpers)
        }

        on("fetching from repository") {
            `when`(mockApi.getCharacters(anyString(), anyString(), anyLong(), anyInt(), anyInt()))
                    .thenReturn(Single.just(getFakeMarvelCharacters()))
            val testObserver = TestObserver<List<CharacterMarvel>>()
            repository.fetchCharacters()
                    .subscribe(testObserver)

            it("should receive 1 value") {
                testObserver.assertValueCount(1)
            }
            it("should not error") {
                testObserver.assertNoErrors()
            }
            it("should complete") {
                testObserver.assertComplete()

            }
        }

        on("fetching from repository gives error") {
            val error = mock(Throwable::class)
            `when`(mockApi.getCharacters(anyString(), anyString(), anyLong(), anyInt(), anyInt()))
                    .thenReturn(Single.error(error))
            val testObserver = TestObserver<List<CharacterMarvel>>()
            repository.fetchCharacters()
                    .subscribe(testObserver)
            it("should error") {
                testObserver.assertError(error)
            }
            it("should emit no values") {
                testObserver.assertValueCount(0)
            }
            it("should complete") {
                //TODO It doesn't show proper error message, ignored, check later
                testObserver.assertComplete()
            }
        }
    }
})

