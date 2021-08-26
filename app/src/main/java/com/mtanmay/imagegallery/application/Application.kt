package com.mtanmay.imagegallery.application

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class Application : Application() {

    companion object {
        private lateinit var mContext: WeakReference<Context>
        fun getContext() = mContext
    }

    override fun onCreate() {
        super.onCreate()
        mContext = WeakReference(applicationContext)
    }

}