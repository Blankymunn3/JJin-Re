package com.jjin_re.features.notice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.model.NoticeModel
import com.jjin_re.repository.NoticeRepository
import com.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class NoticeViewModel: ViewModel() {
    private val noticeRepository = NoticeRepository()
    val list: MutableLiveData<List<NoticeModel>> = MutableLiveData()

    fun downloadNoticeList() {
        viewModelScope.launch {
            noticeRepository.downloadNoticeList(userId = BaseApplication.userModel.userId,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") list.postValue(it.body()!!.data)
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }
}