package com.jjin_re.features.splash

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.util.ObjectUtils
import com.jjin_re.R
import com.jjin_re.databinding.ActivitySplashBinding
import com.jjin_re.features.login.LoginActivity
import com.jjin_re.features.main.MainActivity
import com.jjin_re.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.security.MessageDigest
import com.jjin_re.utils.*

class SplashActivity : BaseActivity() {
    private val binding by binding<ActivitySplashBinding>(R.layout.activity_splash)
    val viewModel by GetViewModel(SplashViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@SplashActivity)
        binding.lifecycleOwner = this@SplashActivity
        binding.viewModel = viewModel
        Utils.setIconTintDark(this@SplashActivity, true)
        BaseApplication.userModel = SharedPreferenceHelper.getUserDataToSharedPreference(this@SplashActivity)
        viewModel.userID.value = BaseApplication.userModel.userId

        BaseApplication.storeVersion = try {
            Jsoup.connect("https://play.google.com/store/apps/details?id=" + "kr.co.takout.ducksticket")
                .timeout(30000)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get()
                .select(".hAyfc .htlgb")[7]
                .ownText()
        } catch (e: Exception) {
            "1.2.6"
        }
        BaseApplication.appVersion = try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).versionName
        } catch (e: Exception) {
            "1.0.0"
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val token = FirebaseInstanceId.getInstance().token
                val setting = getSharedPreferences("setting", MODE_PRIVATE)
                val editor = setting.edit()
                editor.putString("user_token", token)
                editor.apply()
                BaseApplication.firebaseToken = token!!
                viewModel.userToken.postValue(BaseApplication.firebaseToken)
                FirebaseMessaging.getInstance().isAutoInitEnabled = true
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            delay(500)
            doLaunchApp()
        }

        viewModel.responseMessage.observe(this@SplashActivity) {
            if (!ObjectUtils.isEmpty(it)) {
                if (it == "네트워크 상태를 확인해주세요.") finish()
            }
        }
    }

    private fun doLaunchApp() {
        when {
            SharedPreferenceHelper.isSharedAutoLogin(this@SplashActivity) -> {
                viewModel.loadUserInfoFromServer()
            }
            else -> {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
        viewModel.userModel.observe(this@SplashActivity) {
            startMainActivity(it)
        }
    }

    private fun startMainActivity(userModel: UserModel) {
        FirebaseMessaging.getInstance().subscribeToTopic("ALL")
        FirebaseMessaging.getInstance().subscribeToTopic("Android")
        FirebaseMessaging.getInstance().subscribeToTopic("mkt_all")
        FirebaseMessaging.getInstance().subscribeToTopic("mkt_android")
        SharedPreferenceHelper.setUserDataToSharedPreference(this@SplashActivity, userModel)
        SharedPreferenceHelper.setSharedAutoLogin(this@SplashActivity, true)
        finish()
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        try {
            val info : PackageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md : MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash Key :::", something)
            }
        } catch (e: Exception) {
            Log.e("name not found", e.printStackTrace().toString())
        }
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@SplashActivity)
    }
}