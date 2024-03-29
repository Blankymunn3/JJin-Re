package com.jjin_re.features.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.data.ReadReviewData
import com.jjin_re.model.AdBannerModel
import com.jjin_re.model.ReviewModel
import com.jjin_re.repository.ADBannerRepository
import com.jjin_re.repository.DownloadReviewListRepository
import com.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class HomeViewModel: ViewModel() {
    private lateinit var readReviewData: ReadReviewData
    private val downloadADBannerRepository = ADBannerRepository()
    private val downloadReviewListRepository = DownloadReviewListRepository()

    val adBannerList: MutableLiveData<MutableList<AdBannerModel>> = MutableLiveData(ArrayList())
    val reviewList: MutableLiveData<MutableList<ReviewModel>> = MutableLiveData(ArrayList())

    private val isInitialized = MutableLiveData(false)

    fun adBannerDownloadFromServer() {
        viewModelScope.launch {
            downloadADBannerRepository.adBannerList(userId = BaseApplication.userModel.userId,
                onResponse = {
                    if (it.isSuccessful && it.body()!!.code == "200")
                        adBannerList.postValue(it.body()!!.data)
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }

    fun reviewListDownloadFromServer() {
        isInitialized.postValue(true)
        readReviewData = ReadReviewData("")
        viewModelScope.launch {
            downloadReviewListRepository.downloadBestReviewList(readReviewData = readReviewData,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") reviewList.postValue(it.body()!!.data)
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }
}