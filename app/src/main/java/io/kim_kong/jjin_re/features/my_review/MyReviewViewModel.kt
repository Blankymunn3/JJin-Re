package io.kim_kong.jjin_re.features.my_review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.kim_kong.jjin_re.adapter.FragmentAdapter

class MyReviewViewModel: ViewModel() {
    val fragmentAdapter = MutableLiveData<FragmentAdapter>()
}