package com.ragdroid.data.entity

import com.ragdroid.api.entity.TCharacterMarvel
import javax.inject.Inject

/**
 * base mapper interface for mapping
 * Created by garimajain on 18/11/17.
 */
interface Mapper<in I, out O> {

    fun map(input: I): O
}

class CharacterMapper
    @Inject constructor(): Mapper<TCharacterMarvel, CharacterMarvel> {

    override fun map(input: TCharacterMarvel): CharacterMarvel {
        val character = input.let {
            CharacterMarvel(it.id,
                    it.name,
                    it.description,
                    it.thumbnail.path)
        }

        return character;
    }

}