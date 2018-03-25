package com.ragdroid.mvi.models

/**
 * Created by garimajain on 25/03/18.
 */
data class CharacterItemState(
        val characterId: Long,
        val name: String,
        val imageUrl: String,
        val descriptionLabel: String,
        val description: String = "",
        val descriptionLoading: Boolean = false) {

    val shouldShowDescription: Boolean
        get() = description.isNotBlank()

    companion object {
        fun init(characterId: Long,
                 name: String,
                 imageUrl: String,
                 descriptionLabel: String) = CharacterItemState(characterId, name, imageUrl, descriptionLabel)
    }

}