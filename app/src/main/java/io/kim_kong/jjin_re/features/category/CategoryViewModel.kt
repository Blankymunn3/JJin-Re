package io.kim_kong.jjin_re.features.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.model.ReviewModel

class CategoryViewModel: ViewModel() {
    val fragmentAdapter = MutableLiveData<FragmentAdapter>()
}