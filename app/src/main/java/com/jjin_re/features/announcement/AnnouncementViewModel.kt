package com.jjin_re.features.announcement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjin_re.utils.BaseApplication

class AnnouncementViewModel:ViewModel() {
    val appVersion = MutableLiveData("현재 ${BaseApplication.appVersion}")
}