package com.ragdroid.mvi.models

import com.ragdroid.mvi.R
import com.ragdroid.mvi.base.ResourceProvider
import com.ragdroid.mvi.main.MainResult

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


    fun reduce(resources: ResourceProvider, result: MainResult.DescriptionResult): CharacterItemState {
        return when(result) {
            is MainResult.DescriptionResult.DescriptionLoading -> copy(
                    descriptionLabel = resources.getString(R.string.description_loading),
                    description = "",
                    descriptionLoading = true
            )
            is MainResult.DescriptionResult.DescriptionError -> copy(
                    descriptionLoading = false,
                    description = "",
                    descriptionLabel = resources.getString(R.string.description)
            )
            is MainResult.DescriptionResult.DescriptionLoadComplete -> copy(
                    descriptionLabel = resources.getString(R.string.description_loaded),
                    descriptionLoading = false,
                    description = result.description
            )
        }
    }

}