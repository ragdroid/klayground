package com.ragdroid.data

import com.ragdroid.api.MarvelApi
import com.ragdroid.api.entity.TCharacterMarvel
import com.ragdroid.api.entity.TDataWrapper
import com.ragdroid.data.base.Helpers
import com.ragdroid.data.base.SchedulerProvider
import com.ragdroid.data.entity.AppConfig
import com.ragdroid.data.entity.CharacterMapper
import com.ragdroid.data.entity.CharacterMarvel
import io.reactivex.Single
import java.util.concurrent.*
import javax.inject.Inject

/**
 * Created by garimajain on 18/11/17.
 */
class MainRepositoryImpl
    @Inject
    constructor(
            private val marvelApi: MarvelApi,
            private val characterMapper: CharacterMapper,
            private val config: AppConfig,
            private val helpers: Helpers,
            private val schedulerProvider: SchedulerProvider): MainRepository {


    override fun fetchCharacters(): Single<List<CharacterMarvel>> {
        val timeStamp = System.currentTimeMillis()
        return charactersApiSingle(timeStamp)
                .delay(5000, TimeUnit.MILLISECONDS, schedulerProvider.computation())
                .map { dataWrapper: TDataWrapper<List<TCharacterMarvel>> ->
                    val characters = ArrayList<CharacterMarvel>()
                    dataWrapper.data.results
                            .forEach({
                                val characterMarvel = characterMapper.map(it)
                                characters.add(characterMarvel)
                            })
                    return@map characters
                }
    }

    private fun charactersApiSingle(timeStamp: Long): Single<TDataWrapper<List<TCharacterMarvel>>> {
        return marvelApi.getCharacters(
                config.publicKey,
                helpers.buildMD5Digest("" + timeStamp + config.privateKey
                        + config.publicKey),
                timeStamp,
                0,
                10
        )
    }

}

interface MainRepository {

    fun fetchCharacters(): Single<List<CharacterMarvel>>
}
