package com.jjin_re.features.notice

import android.os.Bundle
import android.widget.BaseExpandableListAdapter
import com.jjin_re.R
import com.jjin_re.adapter.NoticeExpandableListAdapter
import com.jjin_re.databinding.ActivityNoticeBinding
import com.jjin_re.model.NoticeModel
import com.jjin_re.utils.BaseActivity
import com.jjin_re.utils.GetViewModel
import com.jjin_re.utils.SharedPreferenceHelper
import com.jjin_re.utils.*

class NoticeActivity: BaseActivity() {
    val binding by binding<ActivityNoticeBinding>(R.layout.activity_notice)
    val viewModel by GetViewModel(NoticeViewModel::class.java)

    private lateinit var noticeExpandableListAdapter: BaseExpandableListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@NoticeActivity)
        setSupportActionBar(binding.tbNotice)
        Utils.setIconTintDark(this@NoticeActivity, true)
        binding.lifecycleOwner = this@NoticeActivity
        binding.viewModel = viewModel
        binding.tbNotice.setNavigationOnClickListener {
            finish()
        }

        binding.rvNotice.setOnGroupExpandListener { groupPosition ->
            val noticeModel: NoticeModel = noticeExpandableListAdapter.getGroup(groupPosition) as NoticeModel
            SharedPreferenceHelper.setLastNoticeCheck(this, noticeModel.uId)
        }

        viewModel.list.observe(this@NoticeActivity, {
            noticeExpandableListAdapter = NoticeExpandableListAdapter(this@NoticeActivity, it)
            binding.rvNotice.setAdapter(noticeExpandableListAdapter)
        })
    }

    override fun onResume() {
        super.onResume()
        actList.remove(this@NoticeActivity)
        viewModel.downloadNoticeList()
    }
}