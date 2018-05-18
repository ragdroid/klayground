package com.ragdroid.mvi.helpers

import com.ragdroid.data.base.Helpers
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

/**
 * Created by garimajain on 18/11/17.
 */
class AndroidHelpers @Inject constructor(): Helpers {
    override fun buildMD5Digest(message: String): String {
        try {
            val messageDigest = MessageDigest.getInstance("MD5")
            val bytes = messageDigest.digest(message.toByteArray())
            val bigInteger = BigInteger(1, bytes)
            var md5 = bigInteger.toString(16)
            while (md5.length < 32) {
                md5 = "0${md5}"
            }
            return md5
        } catch (e: NoSuchAlgorithmException) {
        Timber.e("Error hashing required parameters:${e.message}")
        return ""
    }

    }

}