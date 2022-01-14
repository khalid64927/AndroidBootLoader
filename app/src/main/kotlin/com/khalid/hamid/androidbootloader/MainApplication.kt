package com.khalid.hamid.androidbootloader

import android.app.Application
import android.content.res.Configuration
import android.util.Log

class MainApplication : Application() {

    private val bootModuleManager: BootManager by lazy {
        Log.d("MainApplication", "bootModuleManager")
        BootManager(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MainApplication", "onCreate")
        bootModuleManager.discoverAndInitializeModules(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        bootModuleManager.onLowMemory()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        bootModuleManager.onConfigurationChanged()
    }
}