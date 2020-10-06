package com.jjin_re.features.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.data.PushData
import com.jjin_re.repository.PushChangeRepository
import com.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class AlarmsViewModel: ViewModel() {
    private val pushChangeRepository = PushChangeRepository()
    private lateinit var pushData: PushData
    val eventPush = MutableLiveData(BaseApplication.userModel.eventPush)
    val mktPush = MutableLiveData(BaseApplication.userModel.mktPush)
    val reviewPush = MutableLiveData(BaseApplication.userModel.reviewPush)
    val responseMessage = MutableLiveData("")

    fun pushChange() {
        BaseApplication.userModel.eventPush = eventPush.value!!
        BaseApplication.userModel.mktPush = mktPush.value!!
        BaseApplication.userModel.reviewPush = reviewPush.value!!
        pushData = PushData(userID = BaseApplication.userModel.userId, reviewPush = reviewPush.value!!, mktPush = mktPush.value!!, eventPush = eventPush.value!!)
        viewModelScope.launch {
            pushChangeRepository.pushChange(
                pushData = pushData,
                onResponse = {
                    responseMessage.postValue(it.body()!!.code)
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }
}