package io.kim_kong.jjin_re.features.edit_profile

import android.content.Intent
import android.os.Bundle
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.ActivityEditProfileBinding
import io.kim_kong.jjin_re.features.login.LoginActivity
import io.kim_kong.jjin_re.utils.*

class EditProfileActivity: BaseActivity() {
    val binding by binding<ActivityEditProfileBinding>(R.layout.activity_edit_profile)
    val viewModel by GetViewModel(EditProfileViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@EditProfileActivity)
        Utils.setIconTintDark(this@EditProfileActivity, true)
        setSupportActionBar(binding.tbEditProfile)
        binding.lifecycleOwner = this@EditProfileActivity
        binding.viewModel = viewModel

        binding.tbEditProfile.setNavigationOnClickListener {
            finish()
        }

        viewModel.userModel.postValue(BaseApplication.userModel)

        binding.layoutProfileChange.setOnClickListener {
            SharedPreferenceHelper.clearUserDataToSharedPreference(this@EditProfileActivity)
            SharedPreferenceHelper.setSharedAutoLogin(this@EditProfileActivity, false)
            startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@EditProfileActivity)
    }
}