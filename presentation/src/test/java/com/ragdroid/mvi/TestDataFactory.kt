package com.ragdroid.mvi

import com.ragdroid.data.entity.CharacterMarvel

object TestDataFactory {
    val marcelCharacter1 = CharacterMarvel(1234,
            "Marvel1",
            "Description 1",
            "https://abcd.com/1234")
    val marvelCharacter2 = CharacterMarvel(1235,
            "Marvel2",
            "Description 2",
            "https://abcd.com/1235")
    val mockCharacters: List<CharacterMarvel> = listOf(marcelCharacter1,
            marvelCharacter2)

}