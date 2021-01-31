package com.flybyadmin.View.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flybyadmin.Constant.Constants
import com.flybyadmin.Utils.CommentKeyBoardFix
import com.flybyadmin.Utils.Loader.LocalModel
import com.flybyadmin.Utils.Prefs
import com.flybyadmin.Utils.StringUtils
import com.flybyadmin.Utils.StringUtils.*
import com.flybyadmin.Utils.StringUtils.obj.go_emailID
import com.flybyadmin.Utils.StringUtils.obj.go_firstName
import com.flybyadmin.Utils.StringUtils.obj.go_lastName
import com.flybyadmin.Utils.StringUtils.obj.go_phoneNo
import com.flybyadmin.Utils.StringUtils.obj.go_storeName
import com.flybyadmin.Utils.StringUtils.obj.go_whatsapp
import com.flybyadmin.Utils.StringUtils.obj.intent_one
import com.flybyadmin.Utils.ToastUtils
import com.flybyadmin.View.Interface.AlertTask
import com.flybyadmin.appusekotlin.R
import org.json.JSONObject


class OnBoarding_Step_1 : AppCompatActivity(), View.OnClickListener {
    var ACTIVITY_TITEL: TextView? = null
    var add_next_btn: Button? = null
    var storeNameTV: EditText? = null
    var ownerFirstTV: EditText? = null
    var ownerlastTV: EditText? = null
    var mobileNumberTV: EditText? = null
    var ic_logout: ImageView?=null
    var whatsappTV: EditText? = null
    var emailTV: EditText? = null
    var nextLayout: RelativeLayout? = null
    var mContext: Context? = null
    var mActivity: Activity? = null
    var mPrefs: Prefs?=null
    var allDetails: JSONObject = JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_step_1)
        iniView()
    }

    private fun iniView() {
        mContext = applicationContext
        mActivity = this@OnBoarding_Step_1
        mPrefs= Prefs(mContext!!)
        CommentKeyBoardFix(mActivity!!)
        ACTIVITY_TITEL = findViewById(R.id.ACTIVITY_TITEL)
        add_next_btn = findViewById(R.id.add_next_btn)
        add_next_btn!!.setOnClickListener(this)
        storeNameTV = findViewById(R.id.storeNameTV)
        ic_logout = findViewById(R.id.ic_logout)
        ic_logout!!.setOnClickListener(this)
        ownerFirstTV = findViewById(R.id.ownerFirstTV)
        ownerlastTV = findViewById(R.id.ownerlastTV)
        mobileNumberTV = findViewById(R.id.mobileNumberTV)
        whatsappTV = findViewById(R.id.whatsappTV)
        emailTV = findViewById(R.id.emailTV)
        nextLayout = findViewById(R.id.nextLayout)
        nextLayout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ic_logout->{
                var intent: Intent = Intent(mContext, Admin_Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                mPrefs!!.setString(obj.userID,"")
                mPrefs!!.setBoolean(StringUtils.loginStatus,false)
            }



            R.id.nextLayout -> {
                if (validation()) {
                    var intent: Intent = Intent(mContext, OnBoarding_Step_2::class.java)
                    intent.putExtra(intent_one, allDetails.toString())
                    startActivity(intent)
                }
            }
            R.id.add_next_btn -> {
                Constants.showAlertDialog(
                    mActivity,
                    "All details in form will be lost on cancellation.Are you sure you want to cancel?",
                    "Cancel Onboarding",
                    object : AlertTask {
                        override fun doInPositiveClick() {
                            var intent: Intent = Intent(mContext, OnBoarding_Step_2::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

                        override fun doInNegativeClick() {

                        }

                    })
            }
        }
    }

    private fun validation(): Boolean {
        if (storeNameTV!!.text.toString().trim().isNullOrEmpty()) {

            ToastUtils.shortToast("Please Enter Store Name")
            return false
        }
        if (ownerFirstTV!!.text.toString().trim().isNullOrEmpty()) {

            ToastUtils.shortToast("Please Enter First Name")
            return false
        }
        if (ownerlastTV!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Last Name")
            return false

        }
        if (mobileNumberTV!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Mobile No")
            return false

        }
        if (whatsappTV!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Whatsapp No")
            return false

        }
        if (emailTV!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Email ID")
            return false

        }

        if (whatsappTV!!.text.toString().trim().length != 10) {
            ToastUtils.shortToast("Please Enter Valid Whatsapp No")
            return false

        }
        if (mobileNumberTV!!.text.toString().trim().length != 10) {
            ToastUtils.shortToast("Please Enter Valid Whatsapp No")
            return false

        }
        try {
            allDetails.put(go_storeName, storeNameTV!!.text.toString().trim())
            allDetails.put(go_firstName, ownerFirstTV!!.text.toString().trim())
            allDetails.put(go_lastName, ownerlastTV!!.text.toString().trim())
            allDetails.put(go_phoneNo, mobileNumberTV!!.text.toString().trim())
            allDetails.put(go_whatsapp, whatsappTV!!.text.toString().trim())
            allDetails.put(go_emailID, emailTV!!.text.toString().trim())
        } catch (e: Exception) {
            allDetails = JSONObject()
        }

        return true
    }
}