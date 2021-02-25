package com.flybyadmin.View.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.UwMediaPicker.GalleryMode
import com.flybyadmin.App
import com.flybyadmin.Constant.AppConstants
import com.flybyadmin.Constant.Constants
import com.flybyadmin.Model.Media_Model
import com.flybyadmin.Model.RegistationModel
import com.flybyadmin.Model.VideoUploadModel
import com.flybyadmin.Utils.*
import com.flybyadmin.Utils.Loader.LocalModel
import com.flybyadmin.Utils.PhotoPicker.ImagePickerActivity
import com.flybyadmin.Utils.StringUtils.obj.go_profilePic
import com.flybyadmin.Utils.StringUtils.obj.go_storeMedia
import com.flybyadmin.Utils.StringUtils.obj.go_storeVideo
import com.flybyadmin.View.Interface.AlertTask
import com.flybyadmin.appusekotlin.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class OnBoarding_Step_4 : AppCompatActivity(), View.OnClickListener {
    var ACTIVITY_TITEL: TextView? = null
    var add_next_btn: Button? = null
    var uploadProfilePicture: Button? = null
    var profilePicture: ImageView? = null
    var uploadGaragePictures: Button? = null
    var roundedImageView: ImageView? = null
    var image_count: TextView? = null
    var uploadGarageVideo: Button? = null
    var roundedVideoView: ImageView? = null
    var nextLayout: RelativeLayout? = null
    var mContext: Context? = null
    var mActivity: Activity? = null
    var allDetails: JSONObject = JSONObject()
    var allDetails2: JSONObject = JSONObject()
    var allDetails3: JSONObject = JSONObject()
    var profilePictureList: ArrayList<Media_Model>? = ArrayList<Media_Model>()
    var garagePictureList: ArrayList<Media_Model>? = ArrayList<Media_Model>()
    var videoList: ArrayList<Media_Model>? = ArrayList<Media_Model>()
    var mPrefs: Prefs?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_step_4)
        allDetails = JSONObject(intent.getStringExtra(StringUtils.intent_one))
        allDetails2 = JSONObject(intent.getStringExtra(StringUtils.intent_two))
        allDetails3 = JSONObject(intent.getStringExtra(StringUtils.intent_three))
        iniView()
        checkPermissionActivity()
    }

    private fun checkPermissionActivity() {
        Permissons.Request_CAMERA(mActivity, 120)
        Permissons.Request_STORAGE(mActivity, 569)
    }

    private fun iniView() {
        mContext = applicationContext
        mActivity = this@OnBoarding_Step_4
        CommentKeyBoardFix(mActivity!!)
        mPrefs= Prefs(mContext!!)
        ACTIVITY_TITEL = findViewById(R.id.ACTIVITY_TITEL)
        add_next_btn = findViewById(R.id.add_next_btn)
        add_next_btn!!.setOnClickListener(this)
        uploadProfilePicture = findViewById(R.id.uploadProfilePicture)
        uploadProfilePicture!!.setOnClickListener(this)
        profilePicture = findViewById(R.id.profilePicture)
        uploadGaragePictures = findViewById(R.id.uploadGaragePictures)
        uploadGaragePictures!!.setOnClickListener(this)
        roundedImageView = findViewById(R.id.roundedImageView)
        image_count = findViewById(R.id.image_count)
        uploadGarageVideo = findViewById(R.id.uploadGarageVideo)
        uploadGarageVideo!!.setOnClickListener(this)
        roundedVideoView = findViewById(R.id.roundedVideoView)
        nextLayout = findViewById(R.id.nextLayout)
        nextLayout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.nextLayout -> {
                if (validation()) {
                    hitRegistation();
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
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

                        override fun doInNegativeClick() {

                        }

                    })
            }
            R.id.uploadProfilePicture -> {

                selectPicture(1, AppConstants.PROFILE_PICTURE)

            }
            R.id.uploadGaragePictures -> {

                selectPicture(15, AppConstants.MULTIPLE_GARAGE_PICTURE)

            }
            R.id.uploadGarageVideo -> {

                UwMediaPicker
                    .with(this)
                    .setRequestCode(AppConstants.VIDEO_FOR_GARAGE)
                    .setGalleryMode(GalleryMode.VideoGallery)
                    .setGridColumnCount(3)
                    .setMaxSelectableMediaCount(1)
                    .setCompressionMaxHeight(640f)
                    .setCompressionMaxWidth(640f)
                    .setLightStatusBar(true)
                    .open()

            }
        }
    }

    private fun hitRegistation() {
        LocalModel.instance!!.showProgressDialog(mActivity, "Loading..")
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart(
            StringUtils.go_storeName,
            allDetails.getString(StringUtils.go_storeName)
        )
        builder.addFormDataPart(
            StringUtils.go_firstName,
            allDetails.getString(StringUtils.go_firstName)
        )
        builder.addFormDataPart(
            StringUtils.go_lastName,
            allDetails.getString(StringUtils.go_lastName)
        )
        builder.addFormDataPart(
            StringUtils.go_phoneNo,
            allDetails.getString(StringUtils.go_phoneNo)
        )
        builder.addFormDataPart(
            StringUtils.go_whatsapp,
            allDetails.getString(StringUtils.go_whatsapp)
        )
        builder.addFormDataPart(
            StringUtils.go_emailID,
            allDetails.getString(StringUtils.go_emailID)
        )

        builder.addFormDataPart(
            StringUtils.go_Reason_1,
            allDetails2.getString(StringUtils.go_Reason_1)
        )
        builder.addFormDataPart(
            StringUtils.go_Reason2,
            allDetails2.getString(StringUtils.go_Reason2)
        )
        builder.addFormDataPart(
            StringUtils.go_Reason3,
            allDetails2.getString(StringUtils.go_Reason3)
        )
        builder.addFormDataPart(
            StringUtils.go_serviceCategory,
            allDetails2.getString(StringUtils.go_serviceCategory)
        )

        builder.addFormDataPart(StringUtils.go_state, allDetails3.getString(StringUtils.go_state))
        builder.addFormDataPart(
            StringUtils.go_address,
            allDetails3.getString(StringUtils.go_address)
        )
        builder.addFormDataPart(
            StringUtils.go_latitude,
            allDetails3.getString(StringUtils.go_latitude)
        )
        builder.addFormDataPart(
            StringUtils.go_longitude,
            allDetails3.getString(StringUtils.go_longitude)
        )

        builder.addFormDataPart(
            StringUtils.go_accountType,
            allDetails3.getString(StringUtils.go_accountType)
        )
        builder.addFormDataPart(
            StringUtils.go_planname,
            allDetails3.getString(StringUtils.go_planname)
        )

        builder.addFormDataPart(StringUtils.salePerson_userID, mPrefs!!.getString(StringUtils.userID, ""))

        val fileProfile = File(profilePictureList!!.get(0).loction_Path)
        builder.addFormDataPart(
            go_profilePic, fileProfile.getName(), RequestBody.create(
                MediaType.parse(
                    "multipart/form-data"
                ), fileProfile
            )
        )
        for (i in 0 until garagePictureList!!.size) {
            val file = File(garagePictureList!!.get(i).loction_Path)
            builder.addFormDataPart(
                go_storeMedia, file.getName(), RequestBody.create(
                    MediaType.parse(
                        "multipart/form-data"
                    ), file
                )
            )
        }
        val requestBody = builder.build()
        val call: Call<RegistationModel> =  App.instance!!.apiInterface!!.Registation(requestBody)
        call.enqueue(object : Callback<RegistationModel?> {
            override fun onResponse(
                call: Call<RegistationModel?>?,
                response: Response<RegistationModel?>
            ) {
                LocalModel.instance!!.cancelProgressDialog()
                if (response.body()?.Status.equals("true", true)) {
                    if (videoList!!.size != 0) {
                        hitUploadVideo(response.body()?.garageOwnerID!!)
                        ToastUtils.longToast(response.body()?.msg.toString())
                    } else {
                        ToastUtils.longToast(response.body()?.msg.toString())
                        LocalModel.instance!!.cancelProgressDialog()
                        newBoardingSuccessFull()
                    }
                } else {
                    ToastUtils.shortToast(response.body()?.msg.toString())
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<RegistationModel?>?, t: Throwable?) {
                ToastUtils.shortToast(call.toString())
                LocalModel.instance!!.cancelProgressDialog()
                newBoardingSuccessFull()
            }
        })

    }
    private fun hitUploadVideo(garageOwnerID: String) {
        LocalModel.instance!!.showProgressDialog(mActivity, "Video Uploading Please Wait..")
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart(StringUtils.garageOwnerID, garageOwnerID)
        val fileProfile = File(videoList!!.get(0).loction_Path)
        builder.addFormDataPart(
            go_storeVideo, fileProfile.getName(), RequestBody.create(
                MediaType.parse(
                    "multipart/form-data"
                ), fileProfile
            )
        )
        val requestBody = builder.build()
        val call: Call<VideoUploadModel> =  App.instance!!.apiInterface!!.UploadVideo(requestBody)
        call.enqueue(object : Callback<VideoUploadModel?> {
            override fun onResponse(
                call: Call<VideoUploadModel?>?,
                response: Response<VideoUploadModel?>
            ) {
                LocalModel.instance!!.cancelProgressDialog()
                if (response.body()?.Status.equals("true", true)) {
                    LocalModel.instance!!.cancelProgressDialog()
                    newBoardingSuccessFull()
                } else {
                    newBoardingSuccessFull()
                    ToastUtils.shortToast(response.body()?.msg.toString())
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<VideoUploadModel?>?, t: Throwable?) {
                newBoardingSuccessFull()
                ToastUtils.shortToast(call.toString())
            }
        })

    }

    private fun selectPicture(max: Int, profilePicture: Int) {
        try {
            var intent = Intent(mActivity, ImagePickerActivity::class.java)
            intent.putExtra("Select_Max", max.toString())
            startActivityForResult(intent, profilePicture)

        } catch (e: Exception) {
            ToastUtils.shortToast("ImagePicker Error")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AppConstants.PROFILE_PICTURE -> {
                    var image_uris: ArrayList<Uri> =
                        data!!.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS)
                    profilePictureList!!.clear()
                    for (i in 0 until image_uris.size) {
                        try {
                            val bmOptions = BitmapFactory.Options()
                            val bitmap: Bitmap? = BitmapFactory.decodeFile(
                                image_uris[i].path,
                                bmOptions
                            )
                            val mm = Media_Model(
                                Constants.saveImagetoSDcard(bitmap!!, this@OnBoarding_Step_4)
                                    .toString(), "IMAGE"
                            )
                            profilePictureList!!.add(mm)

                            setMediaConfig(bitmap!!, 1)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }


                }
                AppConstants.MULTIPLE_GARAGE_PICTURE -> {
                    var bitmap: Bitmap? = null
                    var image_uris: ArrayList<Uri> =
                        data!!.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS)
                    garagePictureList!!.clear()
                    for (i in 0 until image_uris.size) {
                        try {
                            val bmOptions = BitmapFactory.Options()
                            bitmap =   BitmapFactory.decodeFile(image_uris[i].path, bmOptions)
                            val mm = Media_Model(
                                Constants.saveImagetoSDcard(
                                    bitmap!!,
                                    this@OnBoarding_Step_4
                                ).toString(), "IMAGE"
                            )
                            garagePictureList!!.add(mm)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                    setMediaConfig(bitmap!!, 2)

                }


                AppConstants.VIDEO_FOR_GARAGE -> {
                    var selectedVideosPathsList =
                        data!!.getStringArrayExtra(UwMediaPicker.UwMediaPickerVideosArrayKey)
                    videoList!!.clear()

                    var file: File = File(selectedVideosPathsList[0])
                    var fileSizeInBytes = file.length()
                    var fileSizeInKB = fileSizeInBytes / 1024
                    var fileSizeInMB = fileSizeInKB / 1024
                    val bitmapt = ThumbnailUtils.createVideoThumbnail(
                        file.getAbsolutePath(),
                        MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
                    )
                    if (fileSizeInMB > 100) {
                        ToastUtils.shortToast("Maximum video size allowed is 100 mb")
                    } else {
                        try {
                        val filePath: String = file.getPath()
                        var bitmap = BitmapFactory.decodeFile(filePath)
                        val mm = Media_Model(
                            selectedVideosPathsList[0].toString(), "VIDEO"
                        )
                        videoList!!.add(mm)

                        setMediaConfig(bitmapt!!, 3)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    }



                }


            }
        }
    }

    private fun setMediaConfig(garagePictureList8: Bitmap, i: Int) {
        when(i){
            1 -> {
                profilePicture!!.setImageBitmap(garagePictureList8)
            }
            2 -> {
                roundedImageView!!.setImageBitmap(garagePictureList8)
                image_count!!.setText(garagePictureList!!.size.toString() + " images")
            }
            3 -> {
                roundedVideoView!!.setImageBitmap(garagePictureList8)
            }
        }

    }

    private fun validation(): Boolean {
        if (profilePictureList!!.size==0){
            ToastUtils.shortToast("Please Select Garage Logo")
            return false
        }else if (garagePictureList!!.size==0){
            ToastUtils.shortToast("Please Select Garage Image")
            return false
        }
        return true
    }

    private fun newBoardingSuccessFull(){
        Constants.showAlertDialogG(
            mActivity,
            "Details sucessfully submitted for approval.",
            "Onboard New Store",
            object : AlertTask {
                override fun doInPositiveClick() {
                    var intent: Intent = Intent(mContext, OnBoarding_Step_1::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                override fun doInNegativeClick() {
                }

            })
    }

}