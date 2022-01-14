package com.khalid.hamid.androidbootloader.initializerOne

import android.app.Application
import android.util.Log
import com.khalid.hamid.androidbootloader.BootModule

class InitializerOneModule: BootModule() {

    override fun onAppStarted(applicationContext: Application) {
        super.onAppStarted(applicationContext)
        Log.d("InitializerOneModule", "InitializerOneModule")
    }
}