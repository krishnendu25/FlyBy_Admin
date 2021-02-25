package com.flybyadmin.Constant

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.text.ClipboardManager
import com.flybyadmin.Model.AddressModel
import com.flybyadmin.View.Interface.AlertTask
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Constants {
    companion object obj {
        fun getCurrentUnixTimeStamps(): String? {
            return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                .toString()
        }

        fun getTimeStampTODateString(timeStamp: String?, Sign: String?): String? {
            return try {
                val unixSeconds = java.lang.Long.valueOf(timeStamp!!)
                val date =
                    Date(unixSeconds * 1000L) // *1000 is to convert seconds to milliseconds
                val sdf =
                    SimpleDateFormat(Sign) // the format of your date
                sdf.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        fun getCityNameFromGeo(
            c: Context?,
            latitide: Double,
            longitude: Double
        ): ArrayList<AddressModel>? {
            var tempList: ArrayList<AddressModel> = arrayListOf()
            var strAdd = ""
            val geocoder = Geocoder(c, Locale.getDefault())
            try {
                val addresses =
                    geocoder.getFromLocation(latitide, longitude, 1)
                if (addresses != null) {
                    var addressModel = AddressModel()
                    addressModel.city = addresses[0].locality
                    addressModel.address = addresses[0].getAddressLine(0)
                    tempList.add(addressModel)
                }
            } catch (e: java.lang.Exception) {
            }
            return tempList
        }

        fun getDateToMilliSecound(dateString: String, dateFormat: String): Long {

            var millisecound = 0
            var sdf = SimpleDateFormat(dateFormat)
            try {
                var mDate = sdf.parse(dateString)
                millisecound = mDate.time.toInt()
                return millisecound.toLong()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return millisecound.toLong()
        }

        //Check Device Time Automatic oR Not
        fun isTimeAutomaticOnDevice(c: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Settings.Global.getInt(c.contentResolver, Settings.Global.AUTO_TIME, 0) != 1
            } else {
                Settings.System.getInt(c.contentResolver, Settings.System.AUTO_TIME, 0) != 1
            }
        }

        fun showAlertDialog(c: Context?, body: String?, title: String?, alertTask: AlertTask) {
            val alertDialogBuilder: AlertDialog.Builder
            alertDialogBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(c, R.style.Theme_Material_Dialog_Alert)
            } else {
                AlertDialog.Builder(c)
            }
            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setMessage(body)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog.dismiss()
                alertTask.doInPositiveClick()
            }
            alertDialogBuilder.setNegativeButton("CANCLE") { dialog, which ->
                dialog.dismiss()
                alertTask.doInNegativeClick()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }
        fun showAlertDialogG(c: Context?, body: String?, title: String?, alertTask: AlertTask) {
            val alertDialogBuilder: AlertDialog.Builder
            alertDialogBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(c, R.style.Theme_Material_Dialog_Alert)
            } else {
                AlertDialog.Builder(c)
            }
            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setMessage(body)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog.dismiss()
                alertTask.doInPositiveClick()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }

        fun showImageTakeDialog(mActivity: Activity) {


            val alertDialogBuilder: AlertDialog.Builder
            alertDialogBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(mActivity, R.style.Theme_Material_Dialog_Alert)
            } else {
                AlertDialog.Builder(mActivity)
            }
            alertDialogBuilder.setTitle("SELECT A PHOTO")
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton(
                "Take Photo"
            ) { dialog, which ->
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                mActivity.startActivityForResult(cameraIntent, AppConstants.CAMERA_REQUEST)
            }
            alertDialogBuilder.setNegativeButton("Gallery") { dialog, which ->
                dialog.dismiss()
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                mActivity.startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    AppConstants.GALLERY_REQUEST
                )
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }


        fun getRandomValue(isString: Boolean): String? {
            var values: String? = null

            if (isString) {
                val generator = Random()
                val randomStringBuilder = StringBuilder()
                val randomLength = generator.nextInt(4)
                var tempChar: Char
                for (i in 0 until randomLength) {
                    tempChar = (generator.nextInt(96) + 32).toChar()
                    randomStringBuilder.append(tempChar)
                }
                values = randomStringBuilder.toString()
            } else {
                val range = 9 // to generate a single number with this range, by default its 0..9
                val length = 3 // by default length is 4
                val secureRandom = SecureRandom()
                var s = ""
                run {
                    var i = 0
                    while (i < length) {
                        val number = secureRandom.nextInt(range)
                        if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                            i = -1
                            i++
                            continue
                        }
                        s = s + number
                        i++
                    }
                }
                values = s.toString()
            }
            return values
        }

        fun saveImagetoSDcard(img: Bitmap, mActivity: Activity): String? {
            val imagename: String? = getRandomValue(true)
            val mydir = File(
                mActivity.filesDir
                    .toString() + "/" + "DataBind/"
            )
            if (!mydir.exists()) {
                mydir.mkdir()
            }
            val image = File(mydir, "$imagename.jpg")
            var success = false
            val outStream: FileOutputStream
            try {
                outStream = FileOutputStream(image)
                img.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.flush()
                outStream.close()
                success = true
            } catch (e: java.lang.Exception) {
            }
            return if (success) {
                "$mydir/$imagename.jpg"
            } else {
                ""
            }
        }

        fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: java.lang.Exception) {
                // Log exception
                null
            }
        }

        @Throws(IOException::class)
        fun getCorrectlyOrientedImage(
            context: Context,
            photoUri: Uri?
        ): Bitmap? {
            val MAX_IMAGE_DIMENSION = 900
            var `is` = context.contentResolver.openInputStream(photoUri!!)
            val dbo = BitmapFactory.Options()
            dbo.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`is`, null, dbo)
            `is`!!.close()
            val rotatedWidth: Int
            val rotatedHeight: Int
            val orientation: Int =
                getOrientation(context, photoUri)
            if (orientation == 90 || orientation == 270) {
                rotatedWidth = dbo.outHeight
                rotatedHeight = dbo.outWidth
            } else {
                rotatedWidth = dbo.outWidth
                rotatedHeight = dbo.outHeight
            }
            var srcBitmap: Bitmap?
            `is` = context.contentResolver.openInputStream(photoUri)
            if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
                val widthRatio =
                    rotatedWidth.toFloat() / MAX_IMAGE_DIMENSION.toFloat()
                val heightRatio =
                    rotatedHeight.toFloat() / MAX_IMAGE_DIMENSION.toFloat()
                val maxRatio = Math.max(widthRatio, heightRatio)

                // Create the bitmap from file
                val options = BitmapFactory.Options()
                options.inSampleSize = maxRatio.toInt()
                srcBitmap = BitmapFactory.decodeStream(`is`, null, options)
            } else {
                srcBitmap = BitmapFactory.decodeStream(`is`)
            }
            `is`!!.close()

            /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */if (orientation > 0) {
                val matrix = Matrix()
                matrix.postRotate(orientation.toFloat())
                srcBitmap = Bitmap.createBitmap(
                    srcBitmap!!, 0, 0, srcBitmap.width,
                    srcBitmap.height, matrix, true
                )
            }
            return srcBitmap
        }

        fun getOrientation(context: Context, photoUri: Uri?): Int {
            // it's on the external media.
            return try {
                val cursor = context.contentResolver.query(
                    photoUri!!,
                    arrayOf(MediaStore.Images.ImageColumns.ORIENTATION),
                    null,
                    null,
                    null
                )
                if (cursor!!.count != 1) {
                    return -1
                }
                cursor.moveToFirst()
                cursor.getInt(0)
            } catch (e: java.lang.Exception) {
                0
            }
        }

        fun setClipboard(context: Context, text: String?) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.text = text
            } else {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", text)
                clipboard.setPrimaryClip(clip)
            }
        }

        fun convertMillisToDateString(
            timeInMillis: Long,
            DATE_TIME_FORMAT: String?
        ): String? {
            val d = java.util.Date(timeInMillis)
            val sdf = SimpleDateFormat(DATE_TIME_FORMAT)
            return sdf.format(d)
        }


        fun getResizedBitmap(bm: Bitmap): Bitmap? {
            var resizedBitmap: Bitmap? = null
            return try {
                val width = bm.width
                val height = bm.height
                val scaleWidth = 480.toFloat() / width
                val scaleHeight = 640.toFloat() / height
                // create a matrix for the manipulation
                val matrix = Matrix()
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight)
                // recreate the new Bitmap
                resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
                resizedBitmap
            } catch (e: java.lang.Exception) {
                resizedBitmap
            }
        }

    }


}