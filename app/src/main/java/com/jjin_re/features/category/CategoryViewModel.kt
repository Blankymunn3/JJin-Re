package com.jjin_re.features.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjin_re.adapter.FragmentAdapter

class CategoryViewModel: ViewModel() {
    val fragmentAdapter = MutableLiveData<FragmentAdapter>()
}