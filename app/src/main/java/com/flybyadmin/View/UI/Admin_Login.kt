package com.flybyadmin.View.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.flybyadmin.App
import com.flybyadmin.Constant.Constants
import com.flybyadmin.Model.LoginModel
import com.flybyadmin.Utils.Loader.LocalModel
import com.flybyadmin.Utils.Prefs
import com.flybyadmin.Utils.StringUtils
import com.flybyadmin.Utils.StringUtils.obj.userID
import com.flybyadmin.Utils.ToastUtils
import com.flybyadmin.appusekotlin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Admin_Login : AppCompatActivity() , View.OnClickListener {
    var usernameTV: EditText? = null
    var passwordTV: EditText? = null
    var loginBTN: Button? = null
    var mContext:Context?=null
    var mActivity:Activity?=null
    var mPrefs: Prefs?=null
    var unistall:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        iniView()

    }

    private fun iniView() {
        mContext=applicationContext
        mActivity=this@Admin_Login
        usernameTV = findViewById(R.id.usernameTV);
        passwordTV =  findViewById(R.id.passwordTV);
        unistall =  findViewById(R.id.unistall);
        loginBTN =  findViewById(R.id.loginBTN);
        loginBTN!!.setOnClickListener(this)
        unistall!!.setOnClickListener(this)
        mPrefs= Prefs(mContext!!)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            123
        )
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.unistall -> {
                unistallApp()
            }


            R.id.loginBTN -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        123
                    )
                }else{
                    if (validation()) {
                        hitLogin(
                            usernameTV!!.text.toString().trim(),
                            passwordTV!!.text.toString().trim()
                        )
                    }
                }


            }
        }
    }

    private fun hitLogin(userName: String, password: String) {
        LocalModel.instance!!.showProgressDialog(mActivity, "Loading..")
        val requestCall: Call<LoginModel> = App.instance!!.apiInterface!!.getGarageOwnerLogin(
            userName, password, Constants.getRandomValue(
                true
            )
        )
        requestCall.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.Status.equals("true", true)) {
                        LocalModel.instance!!.cancelProgressDialog()
                        mPrefs!!.setString(userID, response.body()?.userID.toString())
                        mPrefs!!.setBoolean(StringUtils.loginStatus, true)
                        var intent: Intent = Intent(mContext, OnBoarding_Step_1::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        ToastUtils.shortToast(response.body()?.msg.toString())
                        LocalModel.instance!!.cancelProgressDialog()
                    }
                } else {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })
    }

    private fun validation(): Boolean {
        if (usernameTV!!.text.toString().trim().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Username")
            return false

        }
        if (passwordTV!!.text.toString().trim().isNullOrEmpty()){
            ToastUtils.shortToast("Please Enter Password")
            return false
        }
    return true
    }

    fun unistallApp(){
        val packageUri: Uri = Uri.parse("package:$packageName")
        val uninstallIntent = Intent(Intent.ACTION_DELETE, packageUri)
        uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, false)
        startActivity(uninstallIntent)
    }


}