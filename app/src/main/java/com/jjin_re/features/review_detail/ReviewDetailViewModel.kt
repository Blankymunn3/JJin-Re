package com.jjin_re.features.review_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.data.ReadReviewData
import com.jjin_re.model.ReviewModel
import com.jjin_re.repository.DownloadReviewListRepository
import com.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class ReviewDetailViewModel: ViewModel() {
    private lateinit var readReviewData: ReadReviewData
    private val downloadReviewListRepository = DownloadReviewListRepository()
    val responseBody: MutableLiveData<ReviewModel> = MutableLiveData()
    private val responseMessage = MutableLiveData("")
    val urlArr: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val uId = MutableLiveData("")

    val myThumbType = MutableLiveData("")

    fun getReviewDetailDataFromServer() {
        readReviewData = ReadReviewData("", "", uId.value!!)
        viewModelScope.launch {
            downloadReviewListRepository.downloadReviewList(readReviewData = readReviewData,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") {
                    it.body()?.let { response ->
                        urlArr.postValue(response.data[0].imgUrl.split("[@]"))
                        responseBody.postValue(response.data[0])
                        responseMessage.postValue(response.message)
                    }
                }
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }

    fun getReviewMyThumb() {
        viewModelScope.launch {
            downloadReviewListRepository.reviewMyThumb(userID = BaseApplication.userModel.userId,
            uid = uId.value!!,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") {
                    myThumbType.postValue(it.body()!!.data)
                }
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }

    fun reviewThumbUpClick() {
        viewModelScope.launch {
            downloadReviewListRepository.reviewThumbUpAndThumbUp(userID = BaseApplication.userModel.userId,
            uid = uId.value!!,
            onResponse = {
                it.body()?.let {body ->
                    urlArr.postValue(body.data[0].imgUrl.split("[@]"))
                    responseBody.postValue(body.data[0])
                    responseMessage.postValue(body.message)
                }
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }

    fun reviewThumbDownClick() {
        viewModelScope.launch {
            downloadReviewListRepository.reviewThumbUpAndThumbDown(userID = BaseApplication.userModel.userId,
                uid = uId.value!!,
                onResponse = {
                    it.body()?.let {body ->
                        urlArr.postValue(body.data[0].imgUrl.split("[@]"))
                        responseBody.postValue(body.data[0])
                        responseMessage.postValue(body.message)
                    }
                },
                onFailure = {
                    it.printStackTrace()
                })
        }
    }
}