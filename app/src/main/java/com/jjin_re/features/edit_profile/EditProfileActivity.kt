package com.jjin_re.features.edit_profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import com.jjin_re.R
import com.jjin_re.databinding.ActivityEditProfileBinding
import com.jjin_re.features.login.LoginActivity
import com.jjin_re.utils.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditProfileActivity: BaseActivity() {
    val binding by binding<ActivityEditProfileBinding>(R.layout.activity_edit_profile)
    val viewModel by GetViewModel(EditProfileViewModel::class.java)

    private lateinit var matcher: Matcher

    lateinit var name : RequestBody
    lateinit var body: MultipartBody.Part
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.menu_logout -> {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_all")
                FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_android")
                FirebaseMessaging.getInstance().unsubscribeFromTopic("event_all")
                FirebaseMessaging.getInstance().unsubscribeFromTopic("event_android")
                SharedPreferenceHelper.clearUserDataToSharedPreference(this@EditProfileActivity)
                SharedPreferenceHelper.setSharedAutoLogin(this@EditProfileActivity, false)
                startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
                clearActivity()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@EditProfileActivity)
        Utils.setIconTintDark(this@EditProfileActivity, true)
        setSupportActionBar(binding.tbEditProfile)
        binding.lifecycleOwner = this@EditProfileActivity
        binding.viewModel = viewModel
        binding.isVisible = BaseApplication.userModel.type == 0

        binding.tbEditProfile.setNavigationOnClickListener {
            finish()
        }

        Utils.visibleDeleteButton(
            this@EditProfileActivity,
            binding.etEditProfileName,
            binding.delEdtName,
            viewModel.userNickName
        )
        Utils.visibleDeleteButton(
            this@EditProfileActivity,
            binding.etEditProfilePw,
            binding.delEdtPw,
            viewModel.userPW
        )
        Utils.visibleDeleteButton(
            this@EditProfileActivity,
            binding.etEditProfilePwCrm,
            binding.delEdtPwConfirm,
            viewModel.userPwConFirm
        )

        binding.etEditProfilePhone.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.delEdtPhone.visibility = View.GONE
                    val pattern: Pattern = Pattern.compile("([0-9]{3})([0-9]{4})([0-9]{4})")
                    matcher = pattern.matcher(binding.etEditProfilePhone.text.toString())
                    if (!matcher.matches()) {
                        Utils.showSnackBar("전화번호 형식으로 입력해주세요.", binding.root, true)
                        viewModel.phoneCheck.value = false
                    } else {
                        viewModel.phoneCheck.value = true
                    }
                } else {
                    viewModel.userPhone.observe(this@EditProfileActivity) {
                        if (it.isNotEmpty()) binding.delEdtPhone.visibility = View.VISIBLE
                        else binding.delEdtPhone.visibility = View.GONE
                    }
                }
            }
        binding.etEditProfilePhone.setOnKeyListener { _, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.delEdtPhone.visibility = View.GONE
                val pattern: Pattern = Pattern.compile("([0-9]{3})([0-9]{4})([0-9]{4})")
                matcher = pattern.matcher(binding.etEditProfilePhone.text.toString())
                if (!matcher.matches()) {
                    Utils.showSnackBar("전화번호 형식으로 입력해주세요.", binding.root, true)
                    viewModel.phoneCheck.value = false
                } else {
                    viewModel.phoneCheck.value = true
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        viewModel.isSaveButtonEnabled.observe(this@EditProfileActivity) {
            binding.btnChangeProfile.isEnabled = it
        }

        viewModel.responseCode.observe(this@EditProfileActivity, {
            if (!it.isNullOrEmpty()) {
                if (it == "200") {
                    Utils.showToast("사용자 정보가 변경되었습니다.", this@EditProfileActivity)
                    SharedPreferenceHelper.setUserDataToSharedPreference(
                        this@EditProfileActivity,
                        viewModel.userModel.value!!
                    )
                    finish()
                } else {
                    Log.e("error code :: ", it)
                }
            }
        })

        viewModel.userImg.observe(this@EditProfileActivity, {
            if (!it.isNullOrEmpty() && it != BaseApplication.userModel.userImg) viewModel.isSaveButtonEnabled.postValue(true)
        })
    }

    fun onClickDeleteEditText(view: View) {
        when (view.id) {
            R.id.del_edt_name -> binding.etEditProfileName.setText("")
            R.id.del_edt_pw -> binding.etEditProfilePw.setText("")
            R.id.del_edt_pw_confirm -> binding.etEditProfilePwCrm.setText("")
            R.id.del_edt_phone -> binding.etEditProfilePhone.setText("")
        }
    }

    fun onClickProfileImageChange(view: View) {
        checkPermission()
        viewModel.changeImageFlag.postValue(true)
    }

    private fun checkPermission() {
        val listener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d("Permission :: ", "GRANTED")
                onCallBottomPicker()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Log.d("Permission :: ", "DENIED")
                Utils.showToast("권한을 허용해야 프로필사진 변경이 가능합니다.", this@EditProfileActivity)
                finish()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(listener)
            .setRationaleMessage("프로필 사진을 변경하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("권한을 허용하지 않으시면 앱 서비스를 정상적으로 이용할 수 없습니다.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).check()
    }


    @SuppressLint("SetTextI18n")
    fun onCallBottomPicker() {
        TedImagePicker.with(this@EditProfileActivity)
            .title("프로필사진설정")
            .mediaType(MediaType.IMAGE)
            .backButton(R.drawable.ic_arr_back_black)
            .start { uri ->
                viewModel.userImgUri.postValue(uri)
                val path = FileUtil.getPath(uri, this@EditProfileActivity)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                val src = BitmapFactory.decodeFile(path, options)
                val orientation = Utils.getOrientationOfImage(path)
                val changeOrientationSrc = Utils.getRotatedBitmap(src, orientation)
                viewModel.postPhoto.postValue(Photo(changeOrientationSrc!!, isClickable = false, isDelActive = false))
                val file = File(path!!)
                try {
                    val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    body = MultipartBody.Part.createFormData("img", "${BaseApplication.userModel.userId}_${file.name}", requestFile)
                    name = RequestBody.create(MultipartBody.FORM, "${BaseApplication.userModel.userId}_${file.name}")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                viewModel.userImg.postValue(uri.toString())
                viewModel.multipart.postValue(body)
                viewModel.requestBody.postValue(name)
            }
    }
    override fun onPause() {
        super.onPause()
        actList.remove(this@EditProfileActivity)
    }
}