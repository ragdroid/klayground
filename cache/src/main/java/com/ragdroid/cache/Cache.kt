package com.ragdroid.cache

import io.reactivex.Single

/**
 * Base interface for the store classes which store data either in memory / or realm, etc.
 * Created by garima-fueled on 29/12/17.
 */
interface Cache<T> {

    fun putModel(model: T)

    fun clear()

    fun getModel() : T?

    fun getModelWithId(id: String): T?

    fun putModelList(modelList: List<T>): Boolean

    fun getModelList(): List<T>


    //Observables

    fun getModelListObservable(): Single<List<T>>

    fun putModelListObservable(modelList: List<T>): Single<Boolean>

}

/**
 * Extends cache interface and defines basic API for saving paginated data.
 */
interface PageCache<T: PageModel<R>, R: AppModel>: Cache<R> {

    //pagination

    fun putPage(pageModel: T)

    fun getNextPageItems(pageModel: T): List<R>

    fun getNextPage(pageModel: T): T

}