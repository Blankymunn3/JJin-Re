package com.jjin_re.features.main.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jjin_re.R
import com.jjin_re.adapter.ReviewItemRVAdapter
import com.jjin_re.databinding.FragmentHomeBinding
import com.jjin_re.features.edit_review.EditReviewActivity
import com.jjin_re.features.main.MainActivity
import com.jjin_re.features.review_detail.ReviewDetailActivity
import com.jjin_re.model.ReviewModel
import com.jjin_re.utils.*
import com.jjin_re.utils.Extra.EXTRA_REVIEW_UID
import java.util.*

class HomeFragment: Fragment() {
    private lateinit var activity: MainActivity
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by GetViewModel(HomeViewModel::class.java)

    private lateinit var reviewItemRVAdapter: ReviewItemRVAdapter
    private lateinit var adBannerRVAdapter: com.jjin_re.adapter.AdBannerRVAdapter
    private lateinit var requestManager: RequestManager
    private lateinit var linearSnapHelper: LinearSnapHelper
    private lateinit var timerADTask: TimerTask
    private lateinit var timer: Timer
    private var adCurrentIndex = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
        @JvmStatic
        fun newInstance(): HomeFragment = HomeFragment()
    }


    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, null, false)
        binding = FragmentHomeBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        requestManager = Glide.with(this@HomeFragment)
        timer = Timer()

        Utils.showSnackBar("${BaseApplication.userModel.nickName}님 반갑습니다", binding.root, false)

        linearSnapHelper = SnapHelperOneByOne()
        binding.swipeFragmentHome.setOnRefreshListener {
            viewModel.adBannerDownloadFromServer()
            viewModel.reviewListDownloadFromServer()
        }

        adBannerRVAdapter =
            com.jjin_re.adapter.AdBannerRVAdapter(requestManager)
        binding.rvHomeAdBanner.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = SpeedyLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            linearSnapHelper.attachToRecyclerView(binding.rvHomeAdBanner)
            adapter = adBannerRVAdapter
        }

        reviewItemRVAdapter = ReviewItemRVAdapter(requestManager)
        binding.rvReviewItem.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = reviewItemRVAdapter
        }

        reviewItemRVAdapter.setItemClick(object : ReviewItemRVAdapter.ItemClick {
            override fun onClick(uId: String) {
                val intent = Intent(activity, ReviewDetailActivity::class.java)
                intent.putExtra(EXTRA_REVIEW_UID, uId)
                startActivity(intent)
            }
        })

        viewModel.adBannerList.observe(this@HomeFragment, {
            adBannerRVAdapter.setData(it)
        })

        viewModel.reviewList.observe(this@HomeFragment, {
            reviewItemRVAdapter.setData(it)
            if (binding.swipeFragmentHome.isRefreshing) binding.swipeFragmentHome.isRefreshing =
                false
        })
        return binding.root
    }

    private fun makeAdTimerTask(): TimerTask? {
        return object : TimerTask() {
            override fun run() {
                if (adCurrentIndex > -1) binding.rvHomeAdBanner.smoothScrollToPosition(adCurrentIndex)
                adCurrentIndex++
                if (adCurrentIndex > viewModel.adBannerList.value!!.size) adCurrentIndex = 0
            }
        }
    }

    fun timerStart() {
        if (::timerADTask.isInitialized) timerADTask.cancel()
        if (!::timer.isInitialized) timer = Timer()
        timerADTask = makeAdTimerTask()!!
        timer.schedule(timerADTask, 0, 5000)
    }

    fun timerCancel() {
        if (::timerADTask.isInitialized) timerADTask.cancel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.adBannerDownloadFromServer()
        viewModel.reviewListDownloadFromServer()
        timerStart()
    }

    override fun onPause() {
        super.onPause()
        timerCancel()
    }
}