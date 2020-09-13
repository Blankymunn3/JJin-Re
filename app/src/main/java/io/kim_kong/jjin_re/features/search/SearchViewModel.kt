package io.kim_kong.jjin_re.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.data.ReadReviewData
import io.kim_kong.jjin_re.model.ReviewModel
import io.kim_kong.jjin_re.repository.DownloadReviewListRepository
import io.kim_kong.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private lateinit var readReviewData: ReadReviewData
    private val downloadReviewListRepository = DownloadReviewListRepository()
    val reviewList: MutableLiveData<MutableList<ReviewModel>> = MutableLiveData(ArrayList())

    val searchKeyword = MutableLiveData("")
    val page = MutableLiveData(1)
    private val limit = MutableLiveData("20")

    fun searchReviewListDownloadFromServer() {
        readReviewData = ReadReviewData("", "", "", page.value!!, limit.value!!, searchKeyword.value!!)
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