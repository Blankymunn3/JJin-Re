package io.kim_kong.jjin_re.features.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.ActivityMainBinding
import io.kim_kong.jjin_re.features.login.LoginActivity
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.SharedPreferenceHelper
import io.kim_kong.jjin_re.utils.Utils

class MainActivity : BaseActivity() {
    val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel by GetViewModel(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@MainActivity)
        Utils.setIconTintDark(this@MainActivity, true)
        binding.viewModel = viewModel
    }

    fun onClickLogout(view: View) {
        SharedPreferenceHelper.clearUserDataToSharedPreference(this@MainActivity)
        finish()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@MainActivity)
    }
}