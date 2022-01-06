package com.paylater.paylater.utils

import android.content.Context
import android.util.Base64

class Auth {

    fun auth(context: Context) : String{

        val credentials = "${LibSession(context).retrieveLibSession("public_key")}:${LibSession(context).retrieveLibSession("private_key")}"
        val AUTH = Base64.encodeToString(credentials.toByteArray(Charsets.UTF_8), Base64.DEFAULT).replace("\n", "")

        return AUTH
    }
}