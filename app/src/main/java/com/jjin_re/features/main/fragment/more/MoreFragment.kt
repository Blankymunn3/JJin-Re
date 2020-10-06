package com.jjin_re.features.main.fragment.more

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jjin_re.R
import com.jjin_re.databinding.FragmentMoreBinding
import com.jjin_re.features.alarm.AlarmsActivity
import com.jjin_re.features.announcement.AnnouncementActivity
import com.jjin_re.features.category.category_fragment.CategoryFragment
import com.jjin_re.features.edit_profile.EditProfileActivity
import com.jjin_re.features.main.MainActivity
import com.jjin_re.features.my_review.MyReviewActivity
import com.jjin_re.features.notice.NoticeActivity
import com.jjin_re.utils.*


class MoreFragment: Fragment() {
    lateinit var binding : FragmentMoreBinding
    private lateinit var activity: MainActivity
    private val viewModel by GetViewModel(MoreViewModel::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): MoreFragment {
            val fragment = MoreFragment()
            fragment.arguments = bundle
            return fragment
        }

        @JvmStatic
        fun newInstance() = MoreFragment()
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_more, null, false)
        binding = FragmentMoreBinding.bind(view)
        binding.lifecycleOwner = this@MoreFragment
        binding.viewModel = viewModel

        viewModel.lastNoticeCheck.postValue(
            SharedPreferenceHelper.getLastNoticeCheck(activity))

        viewModel.userModel.observe(this@MoreFragment, {
            if (it.userId.isNotEmpty()) viewModel.getUserReviewCnt()
        })

        viewModel.noticeCheck.observe(this@MoreFragment, {
            if (!it) binding.ivHasNewInfo.visibility = View.GONE
            else binding.ivHasNewInfo.visibility = View.VISIBLE
        })
        binding.layoutFragmentMoreProfile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        binding.layoutFragmentMoreAlarm.setOnClickListener {
            startActivity(Intent(activity, AlarmsActivity::class.java))
        }

        binding.layoutFragmentMoreUserReview.setOnClickListener {
            val intent = Intent(activity, MyReviewActivity::class.java)
            intent.putExtra(CategoryFragment.EXTRA_CATEGORY_DATA, "0")
            startActivity(intent)
        }

        binding.layoutFragmentMoreNotice.setOnClickListener {
            startActivity(Intent(activity, NoticeActivity::class.java))
        }

        binding.layoutFragmentMoreAnnouncement.setOnClickListener {
            startActivity(Intent(activity, AnnouncementActivity::class.java))
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.userModel.postValue(BaseApplication.userModel)
    }
}