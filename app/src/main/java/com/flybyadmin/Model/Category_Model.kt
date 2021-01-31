package com.flybyadmin.Model

import android.os.Parcel
import android.os.Parcelable

class Category_Model  {
    var name: String?
    var iD: String?
    var isSelect = false

    constructor(name: String?, ID: String?) {
        this.name = name
        iD = ID
    }
}