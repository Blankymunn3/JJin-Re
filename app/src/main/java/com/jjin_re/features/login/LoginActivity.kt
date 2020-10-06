package com.jjin_re.features.login

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
import com.jjin_re.R
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.jjin_re.databinding.ActivityLoginBinding
import com.jjin_re.features.main.MainActivity
import com.jjin_re.features.signup.SignUpActivity
import com.jjin_re.model.UserModel
import com.jjin_re.utils.*


class LoginActivity: BaseActivity(), LoginComplete {
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

        viewModel.session.postValue(Session.getCurrentSession())


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

        FirebaseMessaging.getInstance().unsubscribeFromTopic("ALL")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Android")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_all")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_android")

        viewModel.session.observe(this@LoginActivity, {
            viewModel.loginComplete.postValue(this@LoginActivity)
        })

        viewModel.loginComplete.observe(this@LoginActivity, {
            viewModel.sessionCallback.postValue(SessionCallback(viewModel.loginComplete.value!!))
        })

        viewModel.sessionCallback.observe(this@LoginActivity, {
            viewModel.session.value!!.addCallback(viewModel.sessionCallback.value)
        })

        viewModel.isSaveButtonEnabled.observe(this@LoginActivity) {
            binding.btnLogin.isEnabled = it
        }

        viewModel.successLoginCommand.observe(this@LoginActivity) {
            userLoginResult(it)
        }
        viewModel.responseMessage.observe(this@LoginActivity) {
            if (!ObjectUtils.isEmpty(it)) Utils.showToast(it, this@LoginActivity)
        }

        Utils.visibleDeleteButton(
            this@LoginActivity,
            binding.etLoginId,
            binding.delEdtId,
            viewModel.userID
        )
        Utils.visibleDeleteButton(
            this@LoginActivity,
            binding.etLoginPassword,
            binding.delEdtPw,
            viewModel.userPW
        )

        viewModel.errorLog.observe(this@LoginActivity) {
            if (it.isNotEmpty()) Utils.showToast(it, this@LoginActivity)
        }
        viewModel.userModel.observe(this@LoginActivity, {
            viewModel.socialLoginWithSocial()
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

    fun onLoginKakaoClick(view: View){
        viewModel.session.value!!.open(AuthType.KAKAO_LOGIN_ALL, this@LoginActivity)
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
                            Log.d(
                                "LoginMainActivity",
                                "signInWithCredential:success ${user!!.displayName}, ${user.displayName}, ${user.email}, ${user.photoUrl.toString()}, ${user.uid}"
                            )
                            val userModel = UserModel()
                            userModel.userId = user.email!!
                            userModel.userPW = user.uid
                            userModel.nickName = user.displayName!!
                            userModel.userImg = user.photoUrl.toString()
                            user.phoneNumber?.let {
                                userModel.phone = user.phoneNumber!!
                            }
                            userModel.type = 1
                            userModel.userToken = BaseApplication.firebaseToken
                            BaseApplication.loginType = "google"
                            userModel.socialType = BaseApplication.loginType
                            viewModel.loginType.postValue(BaseApplication.loginType)
                            viewModel.userModel.postValue(userModel)

                        } else {
                            Log.w(
                                "LoginMainActivity",
                                "signInWithCredential:failure",
                                task.exception
                            )
                        }
                    }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        } else if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@LoginActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(viewModel.sessionCallback.value!!)
    }

    override fun executeAppServiceLogin(loginType: String, nickname: String, email: String, imageUrl: String, userId: String) {
        val userModel = UserModel()
        BaseApplication.loginType = loginType
        userModel.userId = email
        userModel.userPW = userId
        userModel.nickName = nickname
        userModel.userImg = imageUrl
        userModel.type = 1
        userModel.userToken = BaseApplication.firebaseToken
        userModel.socialType = BaseApplication.loginType
        viewModel.loginType.postValue(BaseApplication.loginType)
        viewModel.userModel.postValue(userModel)
    }
}