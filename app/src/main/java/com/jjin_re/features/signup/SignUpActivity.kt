package com.jjin_re.features.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gun0912.tedpermission.util.ObjectUtils
import com.jjin_re.databinding.ActivitySignUpBinding
import com.jjin_re.utils.GetViewModel
import com.jjin_re.utils.SharedPreferenceHelper
import com.jjin_re.features.main.MainActivity
import com.jjin_re.R
import com.jjin_re.features.login.LoginActivity
import com.jjin_re.features.web_view.WebViewActivity
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
            binding.btnSignup.isEnabled = it
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
                        Utils.showSnackBar("전화번호 형식으로 입력해주세요.", binding.layoutSignUpMain, true)
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
    fun onClickCheckBox(view: View) {
        when (view.id) {
            R.id.iv_agree_all -> {
                if (binding.ivAgreeAll.isSelected) {
                    binding.ivAgree1.isSelected = false
                    binding.ivAgree2.isSelected = false
                    binding.ivAgree3.isSelected = false
                    binding.ivAgree4.isSelected = false
                    binding.ivAgreeAll.isSelected = false
                } else {
                    binding.ivAgree1.isSelected = true
                    binding.ivAgree2.isSelected = true
                    binding.ivAgree3.isSelected = true
                    binding.ivAgree4.isSelected = true
                    binding.ivAgreeAll.isSelected = true
                    viewModel.checkBox.postValue(true)
                }
            }
            else -> view.isSelected = !view.isSelected
        }
        if (binding.ivAgree3.isSelected) {
            viewModel.userMktPush.postValue(1)
        } else {
            viewModel.userMktPush.postValue(0)
        }
        if (binding.ivAgree4.isSelected) {
            viewModel.userEventPush.postValue(1)
        } else {
            viewModel.userEventPush.postValue(0)
        }

        viewModel.checkBox.postValue(binding.ivAgree1.isSelected && binding.ivAgree2.isSelected)
    }

    fun agreementClick(view: View) {
        when (view.id) {
            R.id.btn_agree_2_content -> {
                val intent = Intent(this@SignUpActivity, WebViewActivity::class.java)
                intent.putExtra("EXTRA_WEB_VIEW_TITLE", "이용약관")
                intent.putExtra("EXTRA_WEB_VIEW_URL", getString(R.string.str_terms_of_uses_url))
                startActivity(intent)
            }
            R.id.btn_agree_3_content -> {
                val intent = Intent(this@SignUpActivity, WebViewActivity::class.java)
                intent.putExtra("EXTRA_WEB_VIEW_TITLE", "개인정보 취급방침")
                intent.putExtra("EXTRA_WEB_VIEW_URL", getString(R.string.str_privacy_policy_url))
                startActivity(intent)
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