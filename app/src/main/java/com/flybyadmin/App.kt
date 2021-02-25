package com.flybyadmin

import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.flybyadmin.ServiceMannager.ApiClient
import com.flybyadmin.ServiceMannager.ApiInterface
import java.io.IOException


class App : Application() {
    public var apiInterface: ApiInterface? = null
    override fun onCreate() {
        super.onCreate()
        initApplication()
        val pid = Process.myPid()
        val whiteList = "logcat -P '$pid'"
        try {
            Runtime.getRuntime().exec(whiteList).waitFor()
        } catch (e: IOException) {
        } catch (e: Exception) {
        }
        apiInterface = ApiClient.getRetrofit()!!.create(ApiInterface::class.java)
    }

    private fun initApplication() {
        instance = this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    fun getInstance(): App? {
        return instance
    }
    companion object {

        var instance: App? = null
            private set
    }
}