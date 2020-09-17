package com.jjin_re.features.announcement

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.jjin_re.R
import com.jjin_re.databinding.ActivityJjinReAnnouncementBinding
import com.jjin_re.utils.*

class AnnouncementActivity: BaseActivity() {
    val binding by binding<ActivityJjinReAnnouncementBinding>(R.layout.activity_jjin_re_announcement)
    val viewModel by GetViewModel(AnnouncementViewModel::class.java)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setIconTintDark(this@AnnouncementActivity, true)
        actList.add(this@AnnouncementActivity)
        setSupportActionBar(binding.tbAnnouncement)
        binding.lifecycleOwner = this@AnnouncementActivity
        binding.viewModel = viewModel

    }

    fun onToInquireClick(view: View) {
        intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        val address = arrayOf(getString(R.string.inquire_email))
        intent.putExtra(Intent.EXTRA_EMAIL, address)
        intent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name) + " : " + "문의하기")
        intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n\n\n아이디 : ${BaseApplication.userModel.userId}\n앱버전 : ${BaseApplication.appVersion} \nDevice : Android\nDevice Version : ${Build.VERSION.RELEASE}\nDevice Model : ${Build.MODEL}\n")
        startActivity(intent)
    }

    fun onOpenSourceClick(view: View) {
        LibsBuilder()
            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
            .withAboutIconShown(true)
            .withAboutVersionShown(true)
            .withAboutDescription("<b>찐리와 함께 즐거운 시간 보내세요.</b>")
            .start(this@AnnouncementActivity)
    }
}