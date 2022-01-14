package com.khalid.hamid.androidbootloader

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log

@SuppressLint("LogNotTimber")
class BootManager constructor(private val application: Application) {
    internal val discoveredModules = mutableListOf<ModuleInfo>()

    internal lateinit var applicationContext: Application
        private set

    fun discoverAndInitializeModules(applicationContext: Application) {
        this.applicationContext = applicationContext
        Log.d("BootManager", "discoverAndInitializeModules")
        discoveredModules.clear()
        discoverAllModules()
        initializeModules(discoveredModules)
    }

    val hasNoModules:() -> Boolean = {
        if(discoveredModules.isEmpty()) {
            Log.w("BootManager", "No module")
            true
        }
        false
    }

    fun onConfigurationChanged(){
        if(hasNoModules()) return
        discoveredModules.map { it.moduleClass }.forEach {
            (it as BootModule).onConfigurationChanged()
        }
    }

    fun onLowMemory(){
        if(hasNoModules()) return
        discoveredModules.map { it.moduleClass }.forEach {
            (it as BootModule).onLowMemory()
        }

    }

    fun onTerminate(){
        if(hasNoModules()) return
        discoveredModules.map { it.moduleClass }.forEach {
            (it as BootModule).onTerminate()
        }
    }



    internal fun initializeModules(modules: List<ModuleInfo>) {
        modules.forEach { moduleInfo ->
            try {
                val obj = moduleInfo.moduleClass.getDeclaredConstructor().newInstance()
                if (obj !is BootModule) {
                    // Should never come here
                    return@forEach
                }
                obj.onAppStarted(applicationContext)
            } catch (t: Throwable) {
                reportErrorToSentry(t)
                Log.e("ModuleInitializer", "Error initialising module", t)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun discoverAllModules() {
        Log.d("BootManager", "discoverAllModules")
        val moduleList = mutableListOf<ModuleInfo>()

        application.packageManager.queryIntentServices(
            Intent(BootModule.BOOT_MODULE_INTENT),
            PackageManager.GET_META_DATA
        )?.forEach { ri ->
            Log.d("ModuleInitializer", ri.serviceInfo.name)

            ri?.serviceInfo?.let { info ->
                if (!info.name.isNullOrBlank()) {
                    try {
                        val clazz: Class<*> = Class.forName(info.name)
                        if (BootModule::class.java.isAssignableFrom(clazz)) {
                            moduleList.add(
                                ModuleInfo(
                                    clazz as Class<out BootModule>,
                                    info.metaData
                                )
                            )
                        }
                    } catch (t: Throwable) {
                        reportErrorToSentry(t)
                        Log.e("ModuleInitializer", "Error discovering module ${info.name}", t)
                    }
                }
            }
        }

        discoveredModules.clear()
        discoveredModules.addAll(moduleList)
        Log.d("ModuleInitializer", "-------DONE DISCOVERING MODULES--------")
    }

    private fun reportErrorToSentry(t: Throwable) {
        t.printStackTrace()
    }

    internal data class ModuleInfo(
        val moduleClass: Class<out BootModule>,
        val metaData: Bundle?
    )
}