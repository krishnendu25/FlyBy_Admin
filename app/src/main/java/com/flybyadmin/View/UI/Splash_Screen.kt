package com.flybyadmin.View.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.flybyadmin.Utils.Prefs
import com.flybyadmin.Utils.StringUtils.obj.loginStatus
import com.flybyadmin.appusekotlin.R

class Splash_Screen : AppCompatActivity() {
    var mPrefs:Prefs?=null
    var mContext:Context?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniView()
        setContentView(R.layout.activity_splash__screen)
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }
        val handler = Handler()
        val runnable = Runnable {
            if (mPrefs!!.getBoolean(loginStatus,false)) {
                startActivity(Intent(this@Splash_Screen, OnBoarding_Step_1::class.java))
                finish()
            } else {
                    startActivity(Intent(this@Splash_Screen, Admin_Login::class.java))

            }
        }
        handler.postDelayed(runnable, 2100)

    }

    private fun iniView() {
        mContext=applicationContext
        mPrefs= Prefs(mContext!!)
    }
}