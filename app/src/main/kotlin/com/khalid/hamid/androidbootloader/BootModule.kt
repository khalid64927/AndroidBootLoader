package com.khalid.hamid.androidbootloader

import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.IBinder

open class BootModule: Service(), BootLoader {

    companion object {
        const val BOOT_MODULE_INTENT = "com.khalid.hamid.boot.MODULE_INTENT"
    }

    /**
     * If you need to get the application instance, please access it from here. Do not access it by
     * calling getApplication(). Calling getApplication() on BootModule will return null.
     */
    lateinit var applicationContext: Application
        private set

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Can not bind to this service.")
    }

    override fun onCreate() {
        throw UnsupportedOperationException("Can not start this service.")
    }

    override fun onAppStarted(applicationContext: Application) {
        this.applicationContext = applicationContext
    }

    override fun onConfigurationChanged() {

    }

    override fun onTerminate() {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        throw UnsupportedOperationException("Can not start this service")
    }
}