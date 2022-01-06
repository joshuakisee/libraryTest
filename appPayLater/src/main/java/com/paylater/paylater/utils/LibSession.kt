package com.paylater.paylater.utils

import android.content.Context

class LibSession(context: Context) {

    private val libSharedPreference = context.getSharedPreferences("LIB_PREFERENCE_NAME", Context.MODE_PRIVATE)
    private var libEditor = libSharedPreference.edit()

    fun publicKey(key:String){
        libEditor.putString("public_key", key)
        libEditor.commit()
    }

    fun privateKey(key:String){
        libEditor.putString("private_key", key)
        libEditor.commit()
    }

    fun profileIsCreated(state:Boolean){
        libEditor.putBoolean("profile_is_created", state)
        libEditor.commit()
    }

    fun profileFullName(name:String){
        libEditor.putString("full_name", name)
        libEditor.commit()
    }

    fun profilePhone(phone:String){
        libEditor.putString("phone_number", phone)
        libEditor.commit()
    }

    fun profileScore(score:String){
        libEditor.putString("score", score)
        libEditor.commit()
    }

    fun retrieveLibSession(requested:String) :Any? {
        if (requested == "profile_is_created") return libSharedPreference.getBoolean("profile_is_created", false)
        if (requested == "full_name") return libSharedPreference.getString("full_name", null)
        if (requested == "phone_number") return libSharedPreference.getString("phone_number", null)
        if (requested == "score") return libSharedPreference.getString("score", null)
        if (requested == "public_key") return libSharedPreference.getString("public_key", null)
        if (requested == "private_key") return libSharedPreference.getString("private_key", null)
        return null
    }

}