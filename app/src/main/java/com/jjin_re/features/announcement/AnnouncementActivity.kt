package com.jjin_re.features.announcement

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import com.jjin_re.R
import com.jjin_re.databinding.ActivityJjinReAnnouncementBinding
import com.jjin_re.features.web_view.WebViewActivity
import com.jjin_re.utils.BaseActivity
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.GetViewModel
import com.jjin_re.utils.Utils
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder

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
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " : " + "문의하기")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "\n\n\n\n\n\n아이디 : ${BaseApplication.userModel.userId}\n앱버전 : ${BaseApplication.appVersion} \nDevice : Android\nDevice Version : ${Build.VERSION.RELEASE}\nDevice Model : ${Build.MODEL}\n"
        )
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

    fun onClickAnnouncement(view: View) {
        var intent = Intent()
        when (view.id) {
            R.id.layout_terms_of_use -> {
                intent = Intent(this@AnnouncementActivity, WebViewActivity::class.java)
                intent.putExtra("EXTRA_WEB_VIEW_TITLE", "이용약관")
                intent.putExtra("EXTRA_WEB_VIEW_URL", getString(R.string.str_terms_of_uses_url))
            }
            R.id.layout_privacy_policy -> {
                intent = Intent(this@AnnouncementActivity, WebViewActivity::class.java)
                intent.putExtra("EXTRA_WEB_VIEW_TITLE", "개인정보 취급방침")
                intent.putExtra("EXTRA_WEB_VIEW_URL", getString(R.string.str_privacy_policy_url))
            }
            R.id.layout_version_info -> {
                val appPackageName = packageName
                intent = try {
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
                } catch (e: ActivityNotFoundException) {
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                }

            }
        }
        startActivity(intent)
    }
}