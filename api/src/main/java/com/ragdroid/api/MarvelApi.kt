package com.ragdroid.api

import com.ragdroid.api.entity.TCharacterMarvel
import com.ragdroid.api.entity.TComic
import com.ragdroid.api.entity.TDataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by garimajain on 10/11/17.
 */

const val API_KEY = "apikey"
const val HASH = "hash"
const val TIMESTAMP = "ts"
const val OFFSET = "offset"
const val LIMIT = "limit"
const val NAME_STARTS_WITH = "nameStartsWith"
const val CHARACTER_ID = "characterId"

interface MarvelApi {

    @GET("characters")
    fun getCharacters(@Query(API_KEY) publicKey: String,
                      @Query(HASH) md5Digest: String,
                      @Query(TIMESTAMP) timestamp: Long,
                      @Query(OFFSET) offset: Int?,
                      @Query(LIMIT) limit: Int?,
                      @Query(NAME_STARTS_WITH) query: String?): Single<TDataWrapper<List<TCharacterMarvel>>>

    @GET("characters/{characterId}")
    fun getCharacter(@Path(CHARACTER_ID) characterId: Long,
                     @Query(API_KEY) publicKey: String,
                     @Query(HASH) md5Digest: String,
                     @Query(TIMESTAMP) timestamp: Long): Single<TDataWrapper<List<TCharacterMarvel>>>

    /**
     * Retrieve list of comics by character Id
     */
    @GET("characters/{characterId}/{comicType}")
    fun getCharacterComics(@Path("characterId") characterId: Long,
                                    @Path("comicType") comicType: String,
                                    @Query("offset") offset: Int?,
                                    @Query("limit") limit: Int?,
                                    @Query("apikey") publicKey: String,
                                    @Query("hash") md5Digest: String,
                                    @Query("ts") timestamp: Long): Single<TDataWrapper<List<TComic>>>


}