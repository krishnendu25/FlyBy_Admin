package com.flybyadmin.Utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Permissons {

    companion object{
        fun Request_STORAGE(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(
                act!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                code
            )
        }

        fun Request_CAMERA(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(act!!, arrayOf(Manifest.permission.CAMERA), code)
        }

        fun Request_FINE_LOCATION(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(
                act!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                code
            )
        }

        fun Request_READ_SMS(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(act!!, arrayOf(Manifest.permission.READ_SMS), code)
        }

        fun Request_READ_CONTACTS(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(act!!, arrayOf(Manifest.permission.READ_CONTACTS), code)
        }

        fun Request_READ_CALENDAR(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(act!!, arrayOf(Manifest.permission.READ_CALENDAR), code)
        }

        fun Request_RECORD_AUDIO(act: Activity?, code: Int) {
            ActivityCompat.requestPermissions(act!!, arrayOf(Manifest.permission.RECORD_AUDIO), code)
        }

        //Check Permisson
        fun Check_STORAGE(act: Activity?): Boolean {
            val result =
                ContextCompat.checkSelfPermission(act!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun Check_CAMERA(act: Activity?): Boolean {
            val result = ContextCompat.checkSelfPermission(act!!, Manifest.permission.CAMERA)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun Check_FINE_LOCATION(act: Activity?): Boolean {
            val result =
                ContextCompat.checkSelfPermission(act!!, Manifest.permission.ACCESS_FINE_LOCATION)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun Check_READ_SMS(act: Activity?): Boolean {
            val result = ContextCompat.checkSelfPermission(act!!, Manifest.permission.READ_SMS)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun Check_READ_CONTACTS(act: Activity?): Boolean {
            val result = ContextCompat.checkSelfPermission(act!!, Manifest.permission.READ_CONTACTS)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun Check_READ_CALENDAR(act: Activity?): Boolean {
            val result = ContextCompat.checkSelfPermission(act!!, Manifest.permission.READ_CALENDAR)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun Check_RECORD_AUDIO(act: Activity?): Boolean {
            val result = ContextCompat.checkSelfPermission(act!!, Manifest.permission.RECORD_AUDIO)
            return result == PackageManager.PERMISSION_GRANTED
        }
    }
    //Request Permisson

}