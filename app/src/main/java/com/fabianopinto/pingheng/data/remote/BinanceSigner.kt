package com.fabianopinto.pingheng.data.remote

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object BinanceSigner {
    fun sign(data: String, secret: String): String {
        val sha256HMAC = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        sha256HMAC.init(secretKey)
        return sha256HMAC.doFinal(data.toByteArray()).joinToString("") {
            String.format("%02x", it)
        }
    }
}
