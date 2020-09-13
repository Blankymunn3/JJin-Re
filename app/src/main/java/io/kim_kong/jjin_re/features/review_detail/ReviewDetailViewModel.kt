package io.kim_kong.jjin_re.features.review_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kim_kong.jjin_re.data.ReadReviewData
import io.kim_kong.jjin_re.model.ReviewModel
import io.kim_kong.jjin_re.repository.DownloadReviewListRepository
import io.kim_kong.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class ReviewDetailViewModel: ViewModel() {
    private lateinit var readReviewData: ReadReviewData
    private val downloadReviewListRepository = DownloadReviewListRepository()
    val responseBody: MutableLiveData<ReviewModel> = MutableLiveData()
    private val responseMessage = MutableLiveData("")
    val urlArr: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val uId = MutableLiveData("")

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
}