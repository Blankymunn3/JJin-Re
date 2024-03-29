package com.jjin_re.features.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.repository.SignUpRepository
import com.jjin_re.model.UserModel
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.LiveData
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val liveData = LiveData()
    var userModel = UserModel()
    private val signUpRepository = SignUpRepository()
    val successLoginCommand = MutableLiveData<UserModel>()

    val responseMessage = MutableLiveData("")
    private val idCheck = MutableLiveData(false)
    val phoneCheck = MutableLiveData(false)
    val checkBox = MutableLiveData(false)

    val userID = MutableLiveData("")
    val userPW = MutableLiveData("")
    val userPwConFirm = MutableLiveData("")
    val userNickName = MutableLiveData("")
    val userPhone = MutableLiveData("")
    val userMktPush = MutableLiveData(0)
    val userEventPush = MutableLiveData(0)
    val userReviewPush = MutableLiveData(1)
    val isSaveButtonEnabled = liveData.mediatorLiveData(userID, userPW, userPwConFirm, userNickName, userPhone, idCheck, phoneCheck, checkBox) {
        !userID.value.isNullOrEmpty() && !userPW.value.isNullOrEmpty() && !userPwConFirm.value.isNullOrEmpty() && !userNickName.value.isNullOrEmpty()
        !userPhone.value.isNullOrEmpty() && phoneCheck.value!! && idCheck.value!! && checkBox.value!!
    }

    fun userIdCheck() {
        viewModelScope.launch {
            signUpRepository.userIdCheck(userId = userID.value!!,
                userToken = "",
                onResponse = {
                    if (it.isSuccessful) {
                        idCheck.value = it.body()!!.code == "200"
                        responseMessage.value = it.body()!!.message
                    }
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }

    fun handleSignUpNextClick() {
        if (userPW.value!!.length < 8) {
            responseMessage.value = "비밀번호 길이는 최소 8자이상 가능합니다."
        } else {
            if (userPW.value != userPwConFirm.value) {
                responseMessage.value = "비밀번호를 확인해주세요."
            } else {
                userModel.userId = userID.value!!
                userModel.userPW = userPW.value!!
                userModel.nickName = userNickName.value!!
                userModel.userToken = BaseApplication.firebaseToken
                userModel.type = 0
                userModel.mktPush = userMktPush.value!!
                userModel.eventPush = userEventPush.value!!
                userModel.reviewPush = userReviewPush.value!!
                signUpRepository.signUpUser(userModel = userModel,
                    onResponse = {
                        if (it.isSuccessful)
                            if (it.body()!!.code == "200") {
                                responseMessage.value = it.body()!!.message
                                successLoginCommand.value = it.body()!!.data
                            }
                    },
                    onFailure = {
                        it.printStackTrace()
                    })
            }
        }
    }
}