package io.kim_kong.jjin_re.features.main.fragment.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kim_kong.jjin_re.model.ReviewModel
import io.kim_kong.jjin_re.repository.DownloadReviewListRepository
import io.kim_kong.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val downloadReviewListRepository = DownloadReviewListRepository()

    val reviewList: MutableLiveData<MutableList<ReviewModel>> = MutableLiveData(ArrayList())

    fun reviewListDownloadFromServer() {
        viewModelScope.launch {
            downloadReviewListRepository.downloadReviewList(userId = BaseApplication.userModel.userId,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") reviewList.postValue(it.body()!!.data)
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }
}