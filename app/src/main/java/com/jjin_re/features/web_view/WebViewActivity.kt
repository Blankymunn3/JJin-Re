package com.jjin_re.features.web_view

import android.os.Bundle
import com.jjin_re.R
import com.jjin_re.databinding.ActivityWebViewBinding
import com.jjin_re.utils.BaseActivity
import com.jjin_re.utils.GetViewModel
import com.jjin_re.utils.Utils

class WebViewActivity: BaseActivity() {
    val binding by binding<ActivityWebViewBinding>(R.layout.activity_web_view)
    val viewModel by GetViewModel(WebViewViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.tbWebView)
        Utils.setIconTintDark(this@WebViewActivity, true)
        binding.lifecycleOwner = this@WebViewActivity
        binding.viewModel = viewModel

        viewModel.webViewTitle.postValue(intent.getStringExtra("EXTRA_WEB_VIEW_TITLE"))
        viewModel.webViewUrl.postValue(intent.getStringExtra("EXTRA_WEB_VIEW_URL"))
    }
}