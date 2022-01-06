package com.paylater.paylater.utils

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import java.io.IOException
import java.net.SocketTimeoutException

fun uncodedErrors(throwable: Throwable, context: Context) {
    Log.d("throwable_throwable", "$throwable")
    if (throwable is SocketTimeoutException){
        val toast = Toast.makeText(context, "Network error!...please check your connect.", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        return
    }
    // A network error happened
    if (throwable is IOException) {
        val toast = Toast.makeText(context, "System connection Timeout! please try again.", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        return
    }

    // We don't know what happened.
    val toast = Toast.makeText(context, "Oops Something happened! please try again.", Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return
}