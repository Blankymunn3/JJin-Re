package com.jjin_re.features.edit_review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjin_re.data.EditReviewData
import com.jjin_re.data.WriteReviewData
import com.jjin_re.model.ReviewModel
import com.jjin_re.repository.EditReviewRepository
import com.jjin_re.repository.PhotoUploadRepository
import com.jjin_re.repository.WriteReviewRepository
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.Photo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditReviewViewModel: ViewModel() {
    private val editReviewRepository = EditReviewRepository()
    private lateinit var editData: EditReviewData
    private val photoUploadResponse = PhotoUploadRepository()
    val reviewModel: MutableLiveData<ReviewModel> = MutableLiveData()

    val multipartList: MutableLiveData<ArrayList<MultipartBody.Part>> = MutableLiveData(ArrayList())
    val requestBody: MutableLiveData<ArrayList<RequestBody>> = MutableLiveData(ArrayList())

    val postUriList: MutableLiveData<MutableList<Uri>> = MutableLiveData(ArrayList())
    val postPhotoList: MutableLiveData<MutableList<Photo>> = MutableLiveData(ArrayList())

    val editReviewProductName = MutableLiveData("")
    val editReviewCategory = MutableLiveData("")
    val editReviewEtcCategory = MutableLiveData("")
    val editReviewProductRating = MutableLiveData("0.0")
    val editReviewContents = MutableLiveData("")

    val editReviewPhotoList = MutableLiveData("")

    val responseMessage = MutableLiveData("")
    val responseCode = MutableLiveData("")

    fun sendFile() {
        viewModelScope.launch {
            when {
                multipartList.value!!.size > 1 -> {
                    photoUploadResponse.photoUpload(image = multipartList.value!!,
                        name = requestBody.value!!,
                        onResponse = {
                            if (it.isSuccessful && it.body()!!.code == "200") {
                                var photoList = "${reviewModel.value!!.imgUrl}[@]"
                                for (i in it.body()!!.data.indices) {
                                    photoList += "https://be-at-home.s3.amazonaws.com/jjin-re/user/review/${it.body()!!.data[i]}"
                                    if (i < it.body()!!.data.size - 1) {
                                        photoList += "[@]"
                                    }
                                }
                                editReviewPhotoList.postValue(photoList)
                            } else responseMessage.postValue("사진 업로드중 오류가 발생했습니다.")
                        },
                        onFailure = {
                            responseMessage.postValue("사진 업로드중 오류가 발생했습니다.")
                            it.printStackTrace()
                        })
                }
                multipartList.value!!.size == 1 -> {
                    photoUploadResponse.photoOneUpload(image = multipartList.value!!,
                        name = requestBody.value!!,
                        onResponse = {
                            if (it.isSuccessful && it.body()!!.code == "200") {
                                var photoList = reviewModel.value!!.imgUrl
                                photoList += "[@]https://be-at-home.s3.amazonaws.com/jjin-re/user/review/${it.body()!!.data}"
                                editReviewPhotoList.postValue(photoList)
                            } else responseMessage.postValue("사진 업로드중 오류가 발생했습니다.")
                        },
                        onFailure = {
                            responseMessage.postValue("사진 업로드중 오류가 발생했습니다.")
                            it.printStackTrace()
                        })
                }
                multipartList.value!!.size == 0 -> {
                    editReviewPhotoList.postValue(reviewModel.value!!.imgUrl)
                }
            }
        }
    }


    fun editReview() {
        editData = EditReviewData(reviewModel.value!!.uId,
            BaseApplication.userModel.userId, BaseApplication.userModel.nickName, editReviewProductName.value!!, editReviewContents.value!!, editReviewPhotoList.value!!,
            editReviewCategory.value!!, editReviewEtcCategory.value!!, editReviewProductRating.value!!)
        viewModelScope.launch {
            editReviewRepository.editReview(writeReviewData = editData,
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