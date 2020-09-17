package com.jjin_re.features.main.fragment.more

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gun0912.tedpermission.util.ObjectUtils
import com.jjin_re.model.NoticeModel
import com.jjin_re.model.UserModel
import com.jjin_re.repository.NoticeRepository
import com.jjin_re.repository.ReviewCntRepository
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.SharedPreferenceHelper
import com.jjin_re.utils.Utils
import kotlinx.coroutines.launch

class MoreViewModel: ViewModel() {
    private val reviewCntRepository = ReviewCntRepository()
    private val noticeRepository = NoticeRepository()
    val list: MutableLiveData<List<NoticeModel>> = MutableLiveData()
    val userModel: MutableLiveData<UserModel> = MutableLiveData(BaseApplication.userModel)
    val userReviewCnt = MutableLiveData("0")

    val lastNoticeCheck : MutableLiveData<MutableSet<String?>> = MutableLiveData()

    var noticeCheck = MutableLiveData(false)

    fun getUserReviewCnt() {
        viewModelScope.launch {
            reviewCntRepository.downloadReviewCnt(userId = userModel.value!!.userId,
                onResponse = {
                    if (it.isSuccessful && it.body()!!.code == "200") {
                        userReviewCnt.postValue(it.body()!!.data)
                        downloadNoticeList()
                    }
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }
    fun downloadNoticeList() {
        var isCheck = false
        viewModelScope.launch {
            noticeRepository.downloadNoticeList(userId = BaseApplication.userModel.userId,
                onResponse = {
                    if (it.isSuccessful && it.body()!!.code == "200") {
                        list.postValue(it.body()!!.data)
                        if (!ObjectUtils.isEmpty(lastNoticeCheck)) {
                            val lastNoticeString = lastNoticeCheck
                            for (i in it.body()!!.data.indices) {
                                if (Utils.getNoticeDiffTime(it.body()!!.data[i].created_at) < 7 && lastNoticeString.value!!.size != 0) {
                                    for (noticeItem in lastNoticeString.value!!) {
                                        if (noticeItem != it.body()!!.data[i].uId) {
                                            isCheck = true
                                        } else {
                                            isCheck = false
                                            break
                                        }
                                    }
                                } else if (lastNoticeString.value!!.isEmpty() && Utils.getNoticeDiffTime(
                                        it.body()!!.data.get(i).created_at
                                    ) < 7
                                ) {
                                    isCheck = true
                                    break
                                }
                            }
                            noticeCheck.postValue(isCheck)
                        }
                    }
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }
}