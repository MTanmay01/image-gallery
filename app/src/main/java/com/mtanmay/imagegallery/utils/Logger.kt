package com.mtanmay.imagegallery.utils

import android.util.Log
import com.mtanmay.imagegallery.BuildConfig

class Logger {

    companion object {
        fun log(tag: String, msg: String) {
            if (BuildConfig.DEBUG)
                Log.d(tag, msg)
        }
    }
}