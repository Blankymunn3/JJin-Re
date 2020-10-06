package com.jjin_re.features.web_view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewViewModel: ViewModel() {
    val webViewTitle = MutableLiveData("")
    val webViewUrl = MutableLiveData("")
}