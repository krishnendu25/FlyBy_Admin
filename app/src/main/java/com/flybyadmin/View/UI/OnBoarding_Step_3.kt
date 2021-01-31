package com.flybyadmin.View.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.flybyadmin.App
import com.flybyadmin.Constant.Constants
import com.flybyadmin.Model.CITY_MODEL
import com.flybyadmin.Utils.CommentKeyBoardFix
import com.flybyadmin.Utils.Loader.LocalModel
import com.flybyadmin.Utils.Permissons
import com.flybyadmin.Utils.StringUtils
import com.flybyadmin.Utils.ToastUtils
import com.flybyadmin.View.Interface.AlertTask
import com.flybyadmin.appusekotlin.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class OnBoarding_Step_3 : AppCompatActivity() , View.OnClickListener{
    var ACTIVITY_TITEL: TextView? = null
    var add_next_btn: Button? = null
    var state_ed: TextView? = null
    var addressTV: EditText? = null
    var getLatLong: TextView? = null
    var latitutd: TextView? = null
    var longituted: TextView? = null
    var nextLayout: RelativeLayout? = null
    var mContext: Context?=null
    var mActivity: Activity?=null
    var allDetails: JSONObject = JSONObject()
    var allDetails2: JSONObject = JSONObject()
    var allDetails3: JSONObject = JSONObject()
    private val City_List: ArrayList<CITY_MODEL> = ArrayList<CITY_MODEL>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_step_3)
        allDetails = JSONObject(intent.getStringExtra(StringUtils.intent_one))
        allDetails2 = JSONObject(intent.getStringExtra(StringUtils.intent_two))
        iniView()
        checkLocationPermission()
        hit_city()
    }

    private fun iniView() {
        mContext=applicationContext
        mActivity=this@OnBoarding_Step_3
        CommentKeyBoardFix(mActivity!!)
        ACTIVITY_TITEL = findViewById(R.id.ACTIVITY_TITEL);
        add_next_btn =  findViewById(R.id.add_next_btn)
        add_next_btn!!.setOnClickListener(this)
        state_ed = findViewById(R.id.state_ed);
        state_ed!!.setOnClickListener(this)
        addressTV =  findViewById(R.id.addressTV);
        getLatLong = findViewById(R.id.getLatLong);
        getLatLong!!.setOnClickListener(this)
        latitutd = findViewById(R.id.latitutd);
        longituted = findViewById(R.id.longituted);
        nextLayout =  findViewById(R.id.nextLayout);
        nextLayout!!.setOnClickListener(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.nextLayout -> {
                if (validation()) {
                    var intent: Intent = Intent(mContext, OnBoarding_Step_4::class.java)
                    intent.putExtra(StringUtils.intent_one, allDetails.toString())
                    intent.putExtra(StringUtils.intent_two, allDetails2.toString())
                    intent.putExtra(StringUtils.intent_three, allDetails3.toString())
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
            R.id.getLatLong -> {
                if (checkLocationPermission()) {
                    if (gpsOn())
                        if (!mocklocation()) {
                            fusedLocationClient.lastLocation
                                .addOnSuccessListener { location: Location? ->
                                    latitutd!!.setText(location?.latitude.toString())
                                    longituted!!.setText(location?.longitude.toString())
                                }
                        }
                }
            }
            R.id.state_ed -> {
                if (City_List.size > 0) {
                    Show_city_popup()
                } else {
                    hit_city()
                }
            }
        }
    }

    private fun mocklocation(): Boolean {
        if (Settings.Secure.getString(
                mContext!!.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION
            ).equals("0"))
            return false;
        else{
            ToastUtils.shortToast("Please Trun Off Mock Location")
            return true;
        }


    }

    private fun gpsOn(): Boolean {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (statusOfGPS){
            return true
        }else{
            ToastUtils.shortToast("Please Trun On GPS")
            return false
        }

    }

    private fun checkLocationPermission(): Boolean {
        if ( ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ) {
            Permissons.Request_FINE_LOCATION(mActivity, 256)
            return false
        }else{
            return true
        }
    }

    private fun validation(): Boolean {
        if (state_ed!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Select City")
            return false
        }
        if (addressTV!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please EnterAddress")
            return false
        }
        if (latitutd!!.text.toString().trim().isNullOrEmpty() ||  longituted!!.text.toString().trim().isNullOrEmpty()) {
            ToastUtils.shortToast("Please Select Store Location")
            return false
        }
        allDetails3.put(StringUtils.go_state, state_ed!!.tag.toString().trim())
        allDetails3.put(StringUtils.go_address, addressTV!!.text.toString().trim())
        allDetails3.put(StringUtils.go_latitude, latitutd!!.text.toString().trim())
        allDetails3.put(StringUtils.go_longitude, longituted!!.text.toString().trim())

        return true
    }

    
    private fun hit_city() {
        val requestCall: Call<ResponseBody> = App.instance!!.apiInterface!!.GetAllCity()
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
                            City_List.clear()
                            val jsonArray_AllCategory = jsonObject.getJSONArray("ALLCITY")
                            for (i in 0 until jsonArray_AllCategory.length()) {
                                val city_model = CITY_MODEL(
                                    jsonArray_AllCategory.getJSONObject(i).getString("ID")
                                        .toString(),
                                    jsonArray_AllCategory.getJSONObject(i).getString("CITY")
                                        .toString()
                                )
                                City_List.add(city_model)
                            }
                            Collections.reverse(City_List)
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

    private fun Show_city_popup() {
        val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = layoutInflater.inflate(R.layout.popup_window_view, null, false)
        val main_layout_seasonlist = layout.findViewById<LinearLayout>(R.id.main_layout_seasonlist)
        // Creating the PopupWindow
        val popup = PopupWindow(this)
        popup.contentView = layout
        popup.width = state_ed!!.getWidth()
        popup.height = 600
        popup.isFocusable = true
        for (i in City_List.indices) {
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 10, 10, 10)
            val tv = TextView(this)
            tv.layoutParams = params
            tv.setText(City_List[i].cITY.toString().toUpperCase())
            val face = ResourcesCompat.getFont(mActivity!!, R.font.manroperegular)
            tv.setTypeface(face)
            tv.setTextColor(Color.parseColor("#EEEFF5"))
            tv.gravity = View.TEXT_ALIGNMENT_TEXT_START
            tv.setPadding(20, 10, 0, 0)
            tv.textSize = 14f
            tv.isFocusable = true
            val finalI = i
            tv.setOnClickListener {
                state_ed!!.setText(City_List[i].cITY.toString())
                state_ed!!.setTag(City_List[i].iD)
                popup.dismiss()
            }
            main_layout_seasonlist.addView(tv)
        }
        popup.setOnDismissListener { // TODO Auto-generated method stub
            popup.dismiss()
        }
        popup.setBackgroundDrawable(BitmapDrawable())
        popup.showAsDropDown(state_ed)
    }
}