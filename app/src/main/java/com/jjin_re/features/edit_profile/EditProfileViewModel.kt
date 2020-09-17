package com.jjin_re.features.edit_profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.model.UserModel
import com.jjin_re.repository.PhotoUploadRepository
import com.jjin_re.repository.UserEditProfileRepository
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.LiveData
import com.jjin_re.utils.Photo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileViewModel: ViewModel() {
    private val liveData = LiveData()
    private val userEditProfileRepository = UserEditProfileRepository()
    val userModel : MutableLiveData<UserModel> = MutableLiveData(BaseApplication.userModel)

    var userImgUri: MutableLiveData<Uri> = MutableLiveData()
    val multipart: MutableLiveData<MultipartBody.Part> = MutableLiveData()
    val requestBody: MutableLiveData<RequestBody> = MutableLiveData()

    val postPhoto: MutableLiveData<Photo> = MutableLiveData()
    val userImg = MutableLiveData(BaseApplication.userModel.userImg)
    private val userChangeImg = MutableLiveData(BaseApplication.userModel.userImg)

    val userPW = MutableLiveData(BaseApplication.userModel.userPW)
    val userPwConFirm = MutableLiveData(BaseApplication.userModel.userPW)
    val userNickName = MutableLiveData(BaseApplication.userModel.nickName)
    val userPhone = MutableLiveData(BaseApplication.userModel.phone)
    val phoneCheck = MutableLiveData(false)

    val responseCode = MutableLiveData("")

    val changeImageFlag = MutableLiveData(false)

    val isSaveButtonEnabled = liveData.mediatorLiveData(userPW, userPwConFirm, userNickName, userPhone, phoneCheck) {
        !userPW.value.isNullOrEmpty() && !userPwConFirm.value.isNullOrEmpty() && !userNickName.value.isNullOrEmpty()
        !userPhone.value.isNullOrEmpty() && phoneCheck.value!!
    }

    fun sendProfileImage() {
        if (changeImageFlag.value!!) {
            viewModelScope.launch {
                userEditProfileRepository.profileUpload(image = multipart.value!!,
                    name = requestBody.value!!,
                    onResponse = {
                        if (it.isSuccessful && it.body()!!.code == "200") {
                            userModel.value!!.userImg = it.body()!!.data
                            userChangeImg.postValue(it.body()!!.data)
                            editProfileChange()
                        }
                        else Log.e("response err ::", it.body()!!.message)
                    }, onFailure = {
                        it.printStackTrace()
                    })
            }
        } else editProfileChange()
    }


    private fun editProfileChange() {
        userModel.value!!.userPW = userPW.value!!
        userModel.value!!.nickName = userNickName.value!!
        userModel.value!!.phone = userPhone.value!!
        viewModelScope.launch {
            userEditProfileRepository.userEditProfile(userModel = userModel.value!!,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") userModel.postValue(it.body()!!.data)
                responseCode.postValue(it.body()!!.code)
            },
            onFailure = {
                it.printStackTrace()
                responseCode.postValue(it.message)
            })
        }
    }
}