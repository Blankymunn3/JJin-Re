package io.kim_kong.jjin_re.features.main

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.features.main.fragment.home.HomeFragment

class MainViewModel : ViewModel() {
    val fragmentActivity = MutableLiveData<FragmentAdapter>()
}