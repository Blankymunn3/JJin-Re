package com.jjin_re.features.my_review.my_review_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.data.ReadReviewData
import com.jjin_re.model.ReviewModel
import com.jjin_re.repository.DownloadReviewListRepository
import com.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class MyReviewFragmentViewModel :ViewModel() {

    private lateinit var readReviewData: ReadReviewData
    private val downloadReviewListRepository = DownloadReviewListRepository()
    val category = MutableLiveData("")
    val reviewList: MutableLiveData<MutableList<ReviewModel>> = MutableLiveData(ArrayList())

    val page = MutableLiveData(1)
    private val limit = MutableLiveData("20")

    fun reviewListDownloadFromServer() {
        readReviewData = ReadReviewData(BaseApplication.userModel.userId, category.value!!, "", page.value!!, limit.value!!, "")
        viewModelScope.launch {
            downloadReviewListRepository.downloadReviewList(readReviewData = readReviewData,
                onResponse = {
                    if (it.isSuccessful && it.body()!!.code == "200") reviewList.postValue(it.body()!!.data)
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }
}