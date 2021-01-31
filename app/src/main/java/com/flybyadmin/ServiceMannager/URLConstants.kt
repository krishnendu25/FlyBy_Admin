package com.flybyadmin.ServiceMannager

import com.flybyadmin.appusekotlin.BuildConfig


class URLConstants {
    companion object {
        const val BASE_URL_API = BuildConfig.BASE_URL
        const val LOGIN = "SalespersonApi/saleslogin"
        const val ALL_SERVICES = "StoreApi/getAllServices"
        const val ALL_CITY = "StoreApi/GetAllCity"
        const val salestoreregistration = "SalespersonApi/salestoreregistration"
        const val salesvideoupload = "SalespersonApi/salesvideoupload"

    }
}