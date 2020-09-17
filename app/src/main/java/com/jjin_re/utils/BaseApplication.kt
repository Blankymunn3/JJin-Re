package com.jjin_re.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.gun0912.tedpermission.util.ObjectUtils
import com.jjin_re.R
import com.jjin_re.model.UserModel

class BaseApplication : Application() {

    private lateinit var progressDialog: AppCompatDialog

    override fun onCreate() {
        super.onCreate()
        DEBUG = isDebuggable(this)

        appVersion = try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).versionName
        } catch (e: Exception) {
            "1.0.0"
        }

        userModel = UserModel()
    }

    private fun isDebuggable(context: Context): Boolean {
        var debuggable = false
        val pm = context.packageManager
        try {
            val appInfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = 0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("BaseApplication :::", "${e.printStackTrace()}")
        }
        return debuggable
    }


    companion object {
        var instance = BaseApplication()
        @Volatile
        var DEBUG = false
        var storeVersion = ""
        var firebaseToken = ""
        var loginType = ""
        var appVersion: String? = null
        var userModel: UserModel = UserModel()
        lateinit var requestManager: RequestManager
        lateinit var uploading_anim: LottieAnimationView
    }

    fun progressON(activity: Context, message: String?) {
        if (ObjectUtils.isEmpty(activity) || (activity as Activity).isFinishing) {
            return
        } else {
            requestManager = Glide.with(activity)
        }

        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressSET(message)
        } else {
            progressDialog = AppCompatDialog(activity)
            progressDialog.setCancelable(false)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.progress_loading)
            progressDialog.show()
        }
        uploading_anim = progressDialog.findViewById(R.id.lottie_layer)!!
        uploading_anim.playAnimation()
        uploading_anim.loop(true)
        val tvProgressMessage: TextView =
            progressDialog.findViewById(R.id.tv_progress_message)!!
        if (!TextUtils.isEmpty(message)) {
            tvProgressMessage.text = message
        }
    }

    fun progressSET(message: String?) {
        if (!progressDialog.isShowing) {
            return
        }
        val tvProgressMessage: TextView =
            progressDialog.findViewById(R.id.tv_progress_message)!!
        if (!TextUtils.isEmpty(message)) {
            tvProgressMessage.text = message
        }
    }

    fun progressOFF() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun onDestroy() {
        super.onCreate()
    }
}
