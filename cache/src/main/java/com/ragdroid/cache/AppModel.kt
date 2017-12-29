package com.ragdroid.cache

import java.util.*

/**
 * interface for all cache models to define uniquely identifying key via modelId
 *
 * Created by garima-fueled on 29/12/17.
 */
interface AppModel {
    val modelId: String
}

const val DEFAULT_PAGE_SIZE = 10

/**
 * Base class for paginated model. `MarvelPageModel` extends `PageModel` and defines
 * `Marvel` specific page
 *
 * idPrefix : used for same cache with different Ids.
 * Example a model Request can have Request Details for multiple ids
 */
class PageModel<T: AppModel>(val name: String,
                             val items: List<T>,
                             val pageSize: Int,
                             val idPrefix: String): AppModel {

    constructor(name: String,
                items: List<T>,
                idPrefix: String) : this(name, items, DEFAULT_PAGE_SIZE, idPrefix)

    override val modelId: String
        get() = UUID.randomUUID().toString()
}