package com.jjin_re.features.signup

import android.os.Bundle
import android.view.View
import com.gun0912.tedpermission.util.ObjectUtils
import com.jjin_re.databinding.ActivitySignUpBinding
import com.jjin_re.utils.GetViewModel
import com.jjin_re.utils.SharedPreferenceHelper
import com.jjin_re.features.main.MainActivity
import com.jjin_re.R
import com.jjin_re.features.login.LoginActivity
import com.jjin_re.model.UserModel
import com.jjin_re.utils.BaseActivity
import com.jjin_re.utils.Utils
import java.util.regex.Matcher
import java.util.regex.Pattern
import com.jjin_re.utils.*

class SignUpActivity : BaseActivity() {
    private val binding by binding<ActivitySignUpBinding>(R.layout.activity_sign_up)
    private val viewModel by GetViewModel(SignUpViewModel::class.java)

    private lateinit var matcher: Matcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setIconTintDark(this@SignUpActivity, true)
        actList.add(this@SignUpActivity)
        binding.lifecycleOwner = this@SignUpActivity
        binding.viewModel = viewModel
        binding.isVisibled = true

        viewModel.isSaveButtonEnabled.observe(this@SignUpActivity) {
            binding.btnSignupNext.isEnabled = it
        }

        viewModel.successLoginCommand.observe(this@SignUpActivity) {
            if (!ObjectUtils.isEmpty(it)) onSignUpNextPage()
        }
        viewModel.responseMessage.observe(this@SignUpActivity) {
            if (!ObjectUtils.isEmpty(it)) Utils.showToast(it, this@SignUpActivity)
        }

        Utils.visibleDeleteButton(this@SignUpActivity, binding.etSignupPassword, binding.delEdtPw, viewModel.userPW)
        Utils.visibleDeleteButton(this@SignUpActivity, binding.etSignupPasswordConfirm, binding.delEdtPwConfirm, viewModel.userPwConFirm)
        Utils.visibleDeleteButton(this@SignUpActivity, binding.etSignupNickname, binding.delEdtNickName, viewModel.userNickName)

        binding.etSignupId.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.delEdtId.visibility = View.GONE
                    viewModel.userIdCheck()
                } else {
                    viewModel.userID.observe(this@SignUpActivity) {
                        if (it.isNotEmpty()) binding.delEdtId.visibility = View.VISIBLE
                        else binding.delEdtId.visibility = View.GONE
                    }
                }
            }

        binding.etSignupPhone.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.delEdtPhone.visibility = View.GONE
                    val pattern: Pattern = Pattern.compile("([0-9]{3})([0-9]{4})([0-9]{4})")
                    matcher = pattern.matcher(binding.etSignupPhone.text.toString())
                    if (!matcher.matches()) {
                        Utils.showSnackBar("전화번호 형식으로 입력해주세요.", binding.root, true)
                        viewModel.phoneCheck.value = false
                    } else {
                        viewModel.phoneCheck.value = true
                    }
                } else {
                    viewModel.userPhone.observe(this@SignUpActivity) {
                        if (it.isNotEmpty()) binding.delEdtPhone.visibility = View.VISIBLE
                        else binding.delEdtPhone.visibility = View.GONE
                    }
                }
            }
    }

    fun onClickDeleteEditTextContent(view: View) {
        when (view.id) {
            R.id.del_edt_id -> binding.etSignupId.setText("")
            R.id.del_edt_pw -> binding.etSignupPassword.setText("")
            R.id.del_edt_pw_confirm -> binding.etSignupPasswordConfirm.setText("")
            R.id.del_edt_nick_name -> binding.etSignupNickname.setText("")
            R.id.del_edt_phone -> binding.etSignupPhone.setText("")
        }
    }

    private fun onSignUpNextPage() {
        finish()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@SignUpActivity)
    }
}

/*

        binding.etSignupEmail.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val pattern: Pattern =
                        Pattern.compile("^[0-9a-zA-Z][0-9a-zA-Z_\\-.+]+[0-9a-zA-Z]@[0-9a-zA-Z][0-9a-zA-Z_\\-]*[0-9a-zA-Z](\\.[a-zA-Z]{2,6}){1,2}\$")
                    matcher = pattern.matcher(binding.etSignupEmail.text.toString())
                    if (!matcher.matches()) {
                        Utils.showToast("이메일 형식으로 입력해주세요.", this@SignUpActivity)
                        viewModel.emailCheck.value = false
                    } else {
                        viewModel.emailCheck.value = true
                    }
                }
            }
*/