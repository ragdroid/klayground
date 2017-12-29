package com.ragdroid.cache.repository

import com.ragdroid.cache.AppModel
import com.ragdroid.cache.Cache
import com.ragdroid.cache.PageModel
import io.reactivex.Flowable

/**
 * Created by garima-fueled on 29/12/17.
 */
/**
 *
 * Base Interface for paginated caching and fetching data from cache + server
 * To get the benefit of paginated caching
 * R - core entity (after mapping from server entity) - should implement `AppModel`
 * T - PageModel a class extending from {@link PageModel}
 *
 * Created by garima-fueled on 26/07/17.
 */
interface PaginatedDataRepository<T: PageModel<R>, R: AppModel> {

    /**
     * get page from cache and from server with page no
     *
     * same as `getPageItems(pageNo)` with a slight difference, getPageItems() return Flowable of the page items,
     * whereas getPage(pageNo) returns the page object itself.
     *
     */
    fun getPage(pageNo: Int): Flowable<T>

    /**
     *
     * same as `getPageItemsForId(pageNo)` with a slight difference,
     * getPageItemsForId() return Flowable of the page items,
     * whereas getPageForId(pageNo) returns the page object itself.
     *
     * gets the page from cache and from server for id.
     * If you are interested only in cache then use
     * take(1)
     * Example :
     * If we need to get a resource with particular id. Example
     * GET /requests/{id}, then {id} is idPrefix.
     */
    fun getPageForId(pageNo: Int, idPrefix: String): Flowable<T>

    /**
     * After we fetch first page from server, we get total number of items. This function calculates
     * if there are any items to fetch for a particular page. If the pageNo is 1 it will return true.
     */
    fun isDataAvailableToFetchForPage(pageNo: Int): Boolean

    /**
     * Is the data available to fetch after Items count
     *
     * @param itemCount item count after which we need to check if we have any more data
     */
    fun isDataAvailableToFetchAfter(itemCount: Int): Boolean

    /**
     * same as `getPage(pageNo)` with a slight difference, this function returns Flowable of the page items,
     * whereas getPage(pageNo) returns the page object itself.
     *
     * gets the page either from cache and from server. If you are interested only in cache then use
     * take(1)
     */
    fun getPageItems(pageNo: Int): Flowable<List<R>>

    /**
     *
     * same as `getPageItemsForId(pageNo)` with a slight difference,
     * getPageItemsForId() return Flowable of the page items,
     * whereas getPageForId(pageNo) returns the page object itself.
     *
     * gets the page items from cache and from server for id.
     * If you are interested only in cache then use
     * take(1)
     * Example :
     * If we need to get a resource with particular id. Example
     * GET /requests/{id}, then {id} is idPrefix.
     */
    fun getPageItemsForId(idPrefix: String, pageNo: Int): Flowable<List<R>>

    /**
     * the page size by default
     */
    fun getPageSize(): Int

    /**
     * clears all pages from cache. Can be used for Pull to Refresh
     */
    fun clearCache()

}


//TODO(Garima) : continue wip
interface DefaultPaginatedDataRepository<T: AppModel, S>: PaginatedDataRepository<PageModel<T>, T> {
    val cache: Cache<T>
}