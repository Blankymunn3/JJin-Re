package io.kim_kong.jjin_re.features.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.repository.SplashRepository
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val splashRepository = SplashRepository()

    val userModel = MutableLiveData<UserModel>()
    val userID = MutableLiveData("")

    val responseMessage = MutableLiveData("")

    fun loadUserInfoFromServer() {
        viewModelScope.launch {
            splashRepository.loadUserInfoFromServer(userId = userID.value!!,
            onResponse = {
                if (it.isSuccessful) {
                    if (it.body()!!.code == "204") {
                        userModel.value = it.body()!!.data
                        responseMessage.value = "${userModel.value!!.nickName}님 반갑습니다."
                    }
                } else responseMessage.value = "네트워크 상태를 확인해주세요."
            },
            onFailure = {
                it.printStackTrace()
                responseMessage.value = "네트워크 상태를 확인해주세요."
            })
        }
    }
}