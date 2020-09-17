package com.jjin_re.features.add_review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.data.WriteReviewData
import com.jjin_re.repository.PhotoUploadRepository
import com.jjin_re.repository.WriteReviewRepository
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.Photo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddReviewViewModel : ViewModel() {
    private val writeReviewRepository = WriteReviewRepository()
    private lateinit var writeData: WriteReviewData

    val postUriList: MutableLiveData<MutableList<Uri>> = MutableLiveData(ArrayList())
    val postPhotoList: MutableLiveData<MutableList<Photo>> = MutableLiveData(ArrayList())

    val multipartList: MutableLiveData<ArrayList<MultipartBody.Part>> = MutableLiveData(ArrayList())
    val requestBody: MutableLiveData<ArrayList<RequestBody>> = MutableLiveData(ArrayList())

    private val photoUploadResponse = PhotoUploadRepository()

    val responseMessage = MutableLiveData("")
    val responseCode = MutableLiveData("")

    val addReviewProductName = MutableLiveData("")
    val addReviewCategory = MutableLiveData("")
    val addReviewEtcCategory = MutableLiveData("")
    val addReviewProductRating = MutableLiveData("0.0/5.0")
    val addReviewContents = MutableLiveData("")
    val addReviewPhotoList = MutableLiveData("")

    fun sendFile() {
        viewModelScope.launch {
            photoUploadResponse.photoUpload(image = multipartList.value!!,
                name = requestBody.value!!,
                onResponse = {
                    if (it.isSuccessful && it.body()!!.code == "200") {
                        var photoList = ""
                        for (i in it.body()!!.data.indices) {
                            photoList += "https://be-at-home.s3.amazonaws.com/jjin-re/user/${it.body()!!.data[i]}"
                            if (i < it.body()!!.data.size - 1) {
                                photoList += "[@]"
                            }
                        }
                        addReviewPhotoList.postValue(photoList)
                    } else responseMessage.postValue("사진 업로드중 오류가 발생했습니다.")
                },
                onFailure = {
                    responseMessage.postValue("사진 업로드중 오류가 발생했습니다.")
                    it.printStackTrace()
                })
        }
    }

    fun writeReview() {
        writeData = WriteReviewData(BaseApplication.userModel.userId, BaseApplication.userModel.nickName, addReviewProductName.value!!, addReviewContents.value!!, addReviewPhotoList.value!!,
        addReviewCategory.value!!, addReviewEtcCategory.value!!, addReviewProductRating.value!!)
        viewModelScope.launch {
            writeReviewRepository.writeReview(writeReviewData = writeData,
            onResponse = {
                if (it.isSuccessful && it.body()!!.code == "200") {
                    responseMessage.postValue(it.body()!!.message)
                    responseCode.postValue(it.body()!!.code)
                }
            },
            onFailure = {
                responseMessage.value = it.printStackTrace().toString()
            })
        }
    }
}