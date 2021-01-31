package com.flybyadmin.View.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flybyadmin.App
import com.flybyadmin.Constant.Constants
import com.flybyadmin.Model.Category_Model
import com.flybyadmin.Utils.CommentKeyBoardFix
import com.flybyadmin.Utils.Loader.LocalModel
import com.flybyadmin.Utils.StringUtils
import com.flybyadmin.Utils.StringUtils.obj.go_Reason2
import com.flybyadmin.Utils.StringUtils.obj.go_Reason3
import com.flybyadmin.Utils.StringUtils.obj.go_Reason_1
import com.flybyadmin.Utils.StringUtils.obj.go_serviceCategory
import com.flybyadmin.Utils.StringUtils.obj.intent_one
import com.flybyadmin.Utils.ToastUtils
import com.flybyadmin.View.Adapter.Category_Adapter
import com.flybyadmin.View.Interface.AlertTask
import com.flybyadmin.appusekotlin.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class OnBoarding_Step_2 : AppCompatActivity() , View.OnClickListener{
    var ACTIVITY_TITEL: TextView? = null
    var add_next_btn: Button? = null
    var reason_1: EditText? = null
    var reason_2: EditText? = null
    var reason_3: EditText? = null
    var services_you_provide_list: ListView? = null
    var nextLayout: RelativeLayout? = null
    var mContext: Context?=null
    var mActivity: Activity?=null
    var category_adapter : Category_Adapter?=null
    var allDetails: JSONObject = JSONObject()
    var allDetails2: JSONObject = JSONObject()
    private val Category_List: ArrayList<Category_Model> = ArrayList<Category_Model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_step_2)
        allDetails = JSONObject(intent.getStringExtra(intent_one))
        iniView()
        hit_service()
    }

    private fun iniView() {
        mContext=applicationContext
        mActivity=this@OnBoarding_Step_2
        CommentKeyBoardFix(mActivity!!)
        ACTIVITY_TITEL =  findViewById(R.id.ACTIVITY_TITEL);
        add_next_btn = findViewById(R.id.add_next_btn)
        add_next_btn!!.setOnClickListener(this)
        reason_1 =  findViewById(R.id.reason_1);
        reason_2 =  findViewById(R.id.reason_2);
        reason_3 =  findViewById(R.id.reason_3);
        services_you_provide_list =  findViewById(R.id.services_you_provide_list);
        nextLayout =  findViewById(R.id.nextLayout);
        nextLayout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.nextLayout -> {
                if (validation()) {
                    var intent: Intent = Intent(mContext, OnBoarding_Step_3::class.java)
                    intent.putExtra(StringUtils.intent_one, allDetails.toString())
                    intent.putExtra(StringUtils.intent_two, allDetails2.toString())
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
                            var intent: Intent = Intent(mContext, OnBoarding_Step_1::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }

                        override fun doInNegativeClick() {

                        }

                    });
            }
        }
    }

    private fun validation(): Boolean {

        if (reason_1!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Why visit store - Reason 1")
            return false
        }
        if (reason_2!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Why visit store - Reason 2")
            return false
        }
        if (reason_3!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Enter Why visit store - Reason 3")
            return false
        }
        if (category_adapter!!.get_selected_item().size == 0) {
            ToastUtils.shortToast("Please Select minimum one Services")
            return false
        }

            val separator = ","
            val total: Int = category_adapter!!.get_selected_item().size * separator.length
            val sb = StringBuilder(total)
            for (s in category_adapter!!.get_selected_item()) {
                if (s.isSelect) {
                    sb.append(separator).append(s.iD)
                }
            }
            val serviceCategory = sb.substring(separator.length) // remove leading separator

        allDetails2.put(go_Reason_1,reason_1!!.text.toString().trim())
        allDetails2.put(go_Reason2,reason_2!!.text.toString().trim())
        allDetails2.put(go_Reason3,reason_3!!.text.toString().trim())
        allDetails2.put(go_serviceCategory,serviceCategory.toString().toString())


            return true
    }


    private fun hit_service() {
        val requestCall: Call<ResponseBody> = App.instance!!.apiInterface!!.getAllServices()
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
               
                if (response.isSuccessful) {
                    try {
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(response.body()!!.string())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (jsonObject!!.getString("success").equals("1", ignoreCase = true)) {
                            Category_List.clear()
                            val jsonArray_AllCategory = jsonObject.getJSONArray("AllCategory")
                            for (i in 0 until jsonArray_AllCategory.length()) {
                                val category_model = Category_Model(
                                    jsonArray_AllCategory.getJSONObject(i).getString("Name")
                                        .toString(),
                                    jsonArray_AllCategory.getJSONObject(i).getString("ID")
                                        .toString()
                                )
                                Category_List.add(category_model)
                            }
                            Collections.reverse(Category_List)
                             category_adapter = Category_Adapter(mActivity, Category_List)
                            services_you_provide_list!!.setAdapter(category_adapter)

                        } else {
                           
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                       
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               
            }
        })
    }




}