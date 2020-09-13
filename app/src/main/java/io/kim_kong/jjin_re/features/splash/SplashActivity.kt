package io.kim_kong.jjin_re.features.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.util.ObjectUtils
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.ActivitySplashBinding
import io.kim_kong.jjin_re.features.login.LoginActivity
import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.utils.*
import io.kim_kong.jjin_re.utils.Utils.showSnackBar
import io.kim_kong.jjin_re.utils.Utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val token = FirebaseInstanceId.getInstance().token
                Log.d("IDService", "device token : $token")
                val setting = getSharedPreferences("setting", MODE_PRIVATE)
                val editor = setting.edit()
                editor.putString("fcm_token", token)
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

    override fun onPause() {
        super.onPause()
        actList.remove(this@SplashActivity)
    }
}