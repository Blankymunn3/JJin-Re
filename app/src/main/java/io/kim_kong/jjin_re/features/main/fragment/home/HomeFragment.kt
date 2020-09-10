package io.kim_kong.jjin_re.features.main.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.ReviewItemRVAdapter
import io.kim_kong.jjin_re.databinding.FragmentHomeBinding
import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.SpeedyLinearLayoutManager

class HomeFragment: Fragment() {
    private lateinit var activity: MainActivity
    private val viewModel by GetViewModel(HomeViewModel::class.java)

    private lateinit var reviewItemRVAdapter: ReviewItemRVAdapter
    private lateinit var requestManager: RequestManager

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
        val binding = FragmentHomeBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        requestManager = Glide.with(this@HomeFragment)

        reviewItemRVAdapter = ReviewItemRVAdapter(requestManager)
        binding.rvReviewItem.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = reviewItemRVAdapter
        }

        viewModel.reviewList.observe(this@HomeFragment, {
            reviewItemRVAdapter.setData(it)
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.reviewListDownloadFromServer()
    }
}