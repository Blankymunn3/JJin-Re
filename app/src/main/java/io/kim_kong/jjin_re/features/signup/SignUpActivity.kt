package io.kim_kong.jjin_re.features.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gun0912.tedpermission.util.ObjectUtils
import io.kim_kong.jjin_re.databinding.ActivitySignUpBinding
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.SharedPreferenceHelper
import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.features.login.LoginActivity
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.Utils
import java.util.regex.Matcher

class SignUpActivity : BaseActivity() {
    private val binding by binding<ActivitySignUpBinding>(R.layout.activity_sign_up)
    private val viewModel by GetViewModel(SignUpViewModel::class.java)

    private lateinit var matcher: Matcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@SignUpActivity)
        Utils.setIconTintDark(this@SignUpActivity, true)
        binding.viewModel = viewModel
        binding.isVisibled = true

        viewModel.isSaveButtonEnabled.observe(this) {
            binding.btnSignupNext.isEnabled = it
        }

        viewModel.successLoginCommand.observe(this) {
            if (!ObjectUtils.isEmpty(it)) onSignUpNextPage()
        }
        viewModel.responseMessage.observe(this) {
            if (!ObjectUtils.isEmpty(it)) Utils.showToast(it, this)
        }

        Utils.visibleDeleteButton(this, binding.etSignupId, binding.delEdtId, viewModel.userID)
        Utils.visibleDeleteButton(this, binding.etSignupPassword, binding.delEdtPw, viewModel.userPW)
        Utils.visibleDeleteButton(this, binding.etSignupPasswordConfirm, binding.delEdtPwConfirm, viewModel.userPwConFirm)
        Utils.visibleDeleteButton(this, binding.etSignupNickname, binding.delEdtNickName, viewModel.userNickName)

        binding.etSignupId.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.userIdCheck()
                }
            }
    }



    fun onClickDeleteEditTextContent(view: View) {
        when (view.id) {
            R.id.del_edt_id -> {
                binding.etSignupId.setText("")
            }
            R.id.del_edt_pw -> {
                binding.etSignupPassword.setText("")
            }
            R.id.del_edt_pw_confirm -> {
                binding.etSignupPasswordConfirm.setText("")
            }
            R.id.del_edt_nick_name -> {
                binding.etSignupNickname.setText("")
            }
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
        binding.etSignupPhone.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val pattern: Pattern = Pattern.compile("([0-9]{3})([0-9]{4})([0-9]{4})")
                    matcher = pattern.matcher(binding.etSignupPhone.text.toString())
                    if (!matcher.matches()) {
                        Utils.showToast("전화번호 형식으로 입력해주세요.", this)
                        viewModel.phoneCheck.value = false
                    } else {
                        viewModel.phoneCheck.value = true
                    }
                }
            }
*/