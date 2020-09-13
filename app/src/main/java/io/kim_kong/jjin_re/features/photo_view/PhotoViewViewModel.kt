package io.kim_kong.jjin_re.features.photo_view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestManager

class PhotoViewViewModel:ViewModel() {
    val imgUri: MutableLiveData<List<String>> = MutableLiveData()
    val requestManager: MutableLiveData<RequestManager> = MutableLiveData()


}