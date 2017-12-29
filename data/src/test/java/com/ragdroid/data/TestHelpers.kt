package com.ragdroid.data

import com.nhaarman.mockito_kotlin.createinstance.createInstance
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

/**
 * Created by garimajain on 10/12/17.
 */

//https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
inline fun <reified T : Any> any() = Mockito.any(T::class.java) ?: createInstance<T>()

fun <T> whenever(methodcall: T): OngoingStubbing<T> = Mockito.`when`(methodcall)!!