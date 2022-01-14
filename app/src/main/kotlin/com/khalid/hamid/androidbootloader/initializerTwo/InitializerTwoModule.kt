package com.khalid.hamid.androidbootloader.initializerTwo

import android.app.Application
import android.util.Log
import com.khalid.hamid.androidbootloader.BootModule

class InitializerTwoModule: BootModule() {

    override fun onAppStarted(applicationContext: Application) {
        super.onAppStarted(applicationContext)
        Log.d("InitializerTwoModule", "InitializerTwoModule")

    }
}