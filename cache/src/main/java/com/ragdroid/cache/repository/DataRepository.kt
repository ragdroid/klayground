package com.ragdroid.cache.repository

import com.ragdroid.cache.AppModel
import com.ragdroid.cache.Cache
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Base interface for fetching data from cache and server
 * Created by garima-fueled on 29/12/17.
 */
interface DataRepository<T: AppModel> {

    fun getData(): Flowable<T>

    fun getDataWithId(id: String): Flowable<T>

    fun getDataFromServer(idPrefix: String): Single<T>

    fun clearCache()

}

interface DefaultDataRepository<T: AppModel>: DataRepository<T> {
    val cache: Cache<T>

    override fun getData(): Flowable<T> =
            getDataFromCache()
                    .concatWith(
                            getDataFromServer("")
                                    .doOnSuccess(cache::putModel)
                                    .toMaybe())


    fun getDataFromCache(): Maybe<T> {
        val modelFromCache = cache.getModel() ?: return Maybe.empty()
        return Maybe.just(modelFromCache)
    }

    fun getDataFromCacheWithId(id: String): Maybe<T> {
        val modelFromCache = cache.getModelWithId(id) ?: return Maybe.empty()
        return Maybe.just(modelFromCache)
    }

    override fun getDataWithId(id: String): Flowable<T> =
            getDataFromCacheWithId(id)
                    .concatWith(getDataFromServer(id)
                            .toMaybe()
                            .doOnSuccess(cache::putModel))

    override fun clearCache() = cache.clear()

}
