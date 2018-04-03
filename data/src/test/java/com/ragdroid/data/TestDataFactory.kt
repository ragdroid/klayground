package com.ragdroid.data

import com.ragdroid.api.entity.*
import com.ragdroid.data.entity.Item

/**
 * Created by garimajain on 09/12/17.
 */
fun getFakeMarvelCharacters(): TDataWrapper<List<TCharacterMarvel>> {
    val tDataContainer = TDataContainer(1, 2, 10, 2, listOf(getFakeMarvelCharacter()))
    return TDataWrapper(
            1,
            "sttaus",
            "copyRIght",
            "text",
            "html",
            "etag",
            tDataContainer)
}


fun getFakeMarvelCharacter() = TCharacterMarvel(1234,
        "Name",
        "I am Marvel Character",
        getFakeThumbnail(),
        getFakeCharacterComicWrapper(),
        getFakeCharacterComicWrapper(),
        getFakeCharacterComicWrapper(),
        getFakeCharacterComicWrapper(),
        emptyList()
)


fun getFakeThumbnail() = TImage("https://abcd.com", "jpg")

fun getFakeCharacterComicWrapper() = TCharacterComicWrapper(
        10,
        "https://collections.com/abcd",
        5,
        emptyList()
)

fun getFakeMarvelCharacterData() = Item(
        1234,
        "name",
        "description",
        "https://abcd.com/image"
)