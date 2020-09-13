package io.kim_kong.jjin_re.features.login

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@LoginActivity)
        Utils.setIconTintDark(this@LoginActivity, true)
        binding.lifecycleOwner = this@LoginActivity
        binding.viewModel = viewModel

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

    private fun userLoginResult(userModel: UserModel) {
        SharedPreferenceHelper.setUserDataToSharedPreference(this@LoginActivity, userModel)
        SharedPreferenceHelper.setSharedAutoLogin(this@LoginActivity, true)
        Utils.showToast("${BaseApplication.userModel.nickName}님 반갑습니다.", this@LoginActivity)
        finish()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@LoginActivity)
    }
}