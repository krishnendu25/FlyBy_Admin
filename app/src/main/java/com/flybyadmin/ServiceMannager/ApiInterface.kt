package com.flybyadmin.hoori.controller

import com.flybyadmin.Model.LoginModel
import com.flybyadmin.Model.RegistationModel
import com.flybyadmin.Model.VideoUploadModel
import com.flybyadmin.ServiceMannager.URLConstants.Companion.ALL_CITY
import com.flybyadmin.ServiceMannager.URLConstants.Companion.ALL_SERVICES
import com.flybyadmin.ServiceMannager.URLConstants.Companion.LOGIN
import com.flybyadmin.ServiceMannager.URLConstants.Companion.salestoreregistration
import com.flybyadmin.ServiceMannager.URLConstants.Companion.salesvideoupload
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @FormUrlEncoded
    @POST(LOGIN)
    fun getGarageOwnerLogin(
        @Field("userName") username: String?,
        @Field("passWord") password: String?,
        @Field("deviceID") deviceID: String?
    ): Call<LoginModel>

    @GET(ALL_SERVICES)
    fun getAllServices(): Call<ResponseBody>

    @GET(ALL_CITY)
    fun GetAllCity(): Call<ResponseBody>

    @POST(salestoreregistration)
    fun Registation(@Body file: RequestBody?): Call<RegistationModel>

    @POST(salesvideoupload)
    fun UploadVideo(@Body file: RequestBody?): Call<VideoUploadModel>
}