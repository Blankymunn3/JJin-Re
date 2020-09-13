package io.kim_kong.jjin_re.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.util.ObjectUtils

import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.ActivityLoginBinding
import io.kim_kong.jjin_re.features.signup.SignUpActivity
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.utils.*


class LoginActivity: BaseActivity() {
    private val binding by binding<ActivityLoginBinding>(R.layout.activity_login)
    private val viewModel by GetViewModel(LoginViewModel::class.java)

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@LoginActivity)
        Utils.setIconTintDark(this@LoginActivity, true)
        binding.lifecycleOwner = this@LoginActivity
        binding.viewModel = viewModel


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

        FirebaseMessaging.getInstance().unsubscribeFromTopic("ALL")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Android")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_all")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_android")

        viewModel.isSaveButtonEnabled.observe(this) {
            binding.btnLogin.isEnabled = it
        }

        viewModel.successLoginCommand.observe(this) {
            userLoginResult(it)
        }
        viewModel.responseMessage.observe(this) {
            if (!ObjectUtils.isEmpty(it)) Utils.showToast(it, this@LoginActivity)
        }

        Utils.visibleDeleteButton(this, binding.etLoginId, binding.delEdtId, viewModel.userID)
        Utils.visibleDeleteButton(this, binding.etLoginPassword, binding.delEdtPw, viewModel.userPW)

        viewModel.errorLog.observe(this) {
            if (it.isNotEmpty()) Utils.showToast(it, this)
        }
        viewModel.userModel.observe(this@LoginActivity, {
            viewModel.socialLoginWithGoogle()
        })
    }

    fun onClickDeleteButton(view: View) {
        when (view.id) {
            R.id.del_edt_id -> {
                binding.etLoginId.setText("")
            }
            R.id.del_edt_pw -> {
                binding.etLoginPassword.setText("")
            }
        }
    }

    fun onClickSignUpButton(view: View) {
        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
    }

    fun onLoginGoogleClick(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
    }

    private fun userLoginResult(userModel: UserModel) {
        SharedPreferenceHelper.setUserDataToSharedPreference(this@LoginActivity, userModel)
        SharedPreferenceHelper.setSharedAutoLogin(this@LoginActivity, true)
        Utils.showToast("${BaseApplication.userModel.nickName}님 반갑습니다.", this@LoginActivity)
        finish()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 9001) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            auth = Firebase.auth
            try {
                val idToken = task.getResult(ApiException::class.java)!!.idToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser!!.providerData[1]
                            Log.d("LoginMainActivity", "signInWithCredential:success ${user!!.displayName}, ${user.displayName}, ${user.email}, ${user.photoUrl.toString()}, ${user.uid}")
                            val userModel = UserModel()
                            userModel.userId = user.email!!
                            userModel.userPW = user.uid
                            userModel.nickName = user.displayName!!
                            userModel.userImg = user.photoUrl.toString()
                            user.phoneNumber?.let {
                                userModel.phone = user.phoneNumber!!
                            }
                            userModel.userToken = BaseApplication.firebaseToken
                            BaseApplication.loginType = "google"
                            viewModel.loginType.postValue(BaseApplication.loginType)
                            viewModel.userModel.postValue(userModel)

                        } else {
                            Log.w("LoginMainActivity", "signInWithCredential:failure", task.exception)
                        }
                    }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@LoginActivity)
    }
}