package io.kim_kong.jjin_re.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.repository.LoginRepository
import io.kim_kong.jjin_re.utils.LiveData
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel() {
    private val liveData = LiveData()
    private val loginRepository = LoginRepository()
    private lateinit var loginData: LoginData

    val successLoginCommand = MutableLiveData<UserModel>()
    val responseMessage = MutableLiveData("")

    val userModel: MutableLiveData<UserModel> = MutableLiveData()
    val userID = MutableLiveData("")
    val userPW = MutableLiveData("")
    val loginType = MutableLiveData("")

    val errorLog = MutableLiveData("")

    val isSaveButtonEnabled = liveData.mediatorLiveData(userID, userPW) {
        !userID.value.isNullOrEmpty() && !userPW.value.isNullOrEmpty()
    }


    fun handleLoginClick() {
        loginData = LoginData(userID.value!!, userPW.value!!)
        viewModelScope.launch {
            loginRepository.loginUser(loginData = loginData,
                onResponse = {
                    if (it.isSuccessful) {
                        if (it.body()!!.code == "200") successLoginCommand.value = it.body()!!.data
                        else responseMessage.value = it.body()!!.message
                    }
                },
                onFailure = {
                    responseMessage.value = it.toString()
                }

            )
        }
    }

    fun socialLoginWithGoogle() {
        viewModelScope.launch {
            loginRepository.socialLogin(userModel = userModel.value!!,
                loginType = loginType.value!!,
                onResponse = {
                    if (it.isSuccessful && it.body()!!.code == "200") successLoginCommand.value = it.body()!!.data
                    else responseMessage.value = it.body()!!.message
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }
}