package io.kim_kong.jjin_re.features.add_review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.kim_kong.jjin_re.utils.Photo

class AddReviewViewModel : ViewModel() {
    val postUriList: MutableLiveData<MutableList<Uri>> = MutableLiveData(ArrayList())
    val postPhotoList: MutableLiveData<MutableList<Photo>> = MutableLiveData(ArrayList())

    val addReviewProductName = MutableLiveData("")
    val addReviewEtcCategory = MutableLiveData("")
    val addReviewProductRating = MutableLiveData("0.0/5.0")
}