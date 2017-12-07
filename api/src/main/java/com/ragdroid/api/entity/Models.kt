package com.ragdroid.api.entity

/**
 * Created by garimajain on 10/11/17.
 */
data class TComic(
        val id: Int,
        val title: String,
        val description: Any,
        val startYear: Int,
        val endYear: Int,
        val rating: String,
        val modified: String,
        val name: String,
        val resourceURI: String,
        val thumbnail: TImage,
        val images: List<TImage>
)

data class TDataContainer<T> (
        val offset: Int,
        val limit: Int,
        val total: Int,
        val count: Int,
        val results : T
)

data class TCharacterComicWrapper(
        val available: Int,
        val collectionURI: String,
        val returned: Int,
        val items: List<TComic>? = listOf()
)

data class TDataWrapper<T> (
    val code: Int,
    val status: String,
    val copyRight: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: TDataContainer<T>
)

data class TImage(
        val path:String,
        val extension: String
)

data class TCharacterMarvel(
        val id: Long,
        val name: String,
        val description: String,
        val thumbnail: TImage,
        val comics: TCharacterComicWrapper,
        val series: TCharacterComicWrapper,
        val stories: TCharacterComicWrapper,
        val events: TCharacterComicWrapper,
        val urls: List<TUrl>
)

data class TUrl(
        val type: String,
        val url: String
)