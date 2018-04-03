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
    @Inject constructor(): Mapper<TCharacterMarvel, Item> {

    override fun map(input: TCharacterMarvel): Item {
        val character = input.let {
            Item(it.id,
                    it.name,
                    it.description,
                    "${it.thumbnail.path}.${it.thumbnail.extension}")
        }

        return character;
    }

}