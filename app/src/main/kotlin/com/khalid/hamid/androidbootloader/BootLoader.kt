package com.khalid.hamid.androidbootloader

import android.app.Application

interface BootLoader {
    fun onAppStarted(applicationContext: Application)
    fun onConfigurationChanged()
    fun onLowMemory()
    fun onTerminate()
}