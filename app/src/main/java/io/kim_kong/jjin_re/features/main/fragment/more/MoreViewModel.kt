package io.kim_kong.jjin_re.features.main.fragment.more

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.repository.ReviewCntRepository
import io.kim_kong.jjin_re.utils.BaseApplication
import kotlinx.coroutines.launch

class MoreViewModel: ViewModel() {
    private val reviewCntRepository = ReviewCntRepository()
    val userModel: MutableLiveData<UserModel> = MutableLiveData(BaseApplication.userModel)
    val userReviewCnt = MutableLiveData("0")

    fun getUserReviewCnt() {
        viewModelScope.launch {
            reviewCntRepository.downloadReviewCnt(userId = userModel.value!!.userId,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") userReviewCnt.postValue(it.body()!!.data)
            },
            onFailure = {
                it.printStackTrace()
            })
        }
    }
}