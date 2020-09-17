package com.jjin_re.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.adapter.FragmentAdapter
import com.jjin_re.features.main.fragment.home.HomeFragment
import com.jjin_re.repository.TokenChangeRepository
import com.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val tokenChangeRepository = TokenChangeRepository()
    val fragmentAdapter = MutableLiveData<FragmentAdapter>()
    val responseMessage = MutableLiveData("")
    val userToken = MutableLiveData("")

    fun userTokenChange() {
        viewModelScope.launch {
            tokenChangeRepository.tokenChange(userID = BaseApplication.userModel.userId,
            token = BaseApplication.firebaseToken,
            onResponse = {
                if (!it.isSuccessful && it.body()!!.code != "200")
                    responseMessage.postValue(it.message())
            },
            onFailure = {
                it.printStackTrace()
                responseMessage.postValue(it.message)
            })
        }
    }

}