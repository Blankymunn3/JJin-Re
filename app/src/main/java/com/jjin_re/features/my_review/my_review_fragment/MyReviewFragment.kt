package com.jjin_re.features.my_review.my_review_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jjin_re.R
import com.jjin_re.adapter.ReviewItemRVAdapter
import com.jjin_re.databinding.FragmentMyReviewBinding
import com.jjin_re.features.category.category_fragment.CategoryFragment.Companion.EXTRA_CATEGORY_DATA
import com.jjin_re.features.my_review.MyReviewActivity
import com.jjin_re.features.review_detail.ReviewDetailActivity
import com.jjin_re.utils.*

class MyReviewFragment : Fragment() {

    private lateinit var activity: MyReviewActivity
    private val viewModel by GetViewModel(MyReviewFragmentViewModel::class.java)
    private lateinit var requestManager: RequestManager
    private lateinit var linearSnapHelper: LinearSnapHelper
    private lateinit var reviewItemRVAdapter: ReviewItemRVAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MyReviewActivity
    }

    companion object {
        fun newInstance(bundle: Bundle, category: String): MyReviewFragment {
            val fragment = MyReviewFragment()
            bundle.putString(EXTRA_CATEGORY_DATA, category)
            fragment.arguments = bundle
            return fragment
        }
        @JvmStatic
        fun newInstance(): MyReviewFragment = MyReviewFragment()
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMyReviewBinding.bind(LayoutInflater.from(context).inflate(R.layout.fragment_my_review, null, false))
        binding.rvReviewItem.showShimmerAdapter()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        requestManager = Glide.with(this@MyReviewFragment)
        viewModel.category.postValue(requireArguments().getString(EXTRA_CATEGORY_DATA))
        linearSnapHelper = SnapHelperOneByOne()
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (viewModel.reviewList.value!!.size >= 20) {
                    viewModel.page.postValue(viewModel.page.value!!.plus(1))
                    viewModel.reviewListDownloadFromServer()
                }
            }
        }
        binding.rvReviewItem.addOnScrollListener(scrollListener)

        reviewItemRVAdapter = ReviewItemRVAdapter(requestManager)
        binding.rvReviewItem.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            this.layoutManager = layoutManager
            adapter = reviewItemRVAdapter
        }

        viewModel.category.observe(this@MyReviewFragment, {
            if (!it.isNullOrEmpty())
                viewModel.reviewListDownloadFromServer()
        })

        viewModel.reviewList.observe(this@MyReviewFragment, {
            binding.rvReviewItem.hideShimmerAdapter()
            if (viewModel.reviewList.value!!.size > 0) {
                binding.tvReviewListEmpty.visibility = View.GONE
                binding.lottieEmptyReview.visibility = View.GONE
                binding.lottieEmptyReview.loop(false)
                reviewItemRVAdapter.setData(it)
            } else {
                binding.tvReviewListEmpty.visibility = View.VISIBLE
                binding.lottieEmptyReview.visibility = View.VISIBLE
                binding.lottieEmptyReview.playAnimation()
                binding.lottieEmptyReview.loop(true)
            }
        })

        reviewItemRVAdapter.setItemClick(object : ReviewItemRVAdapter.ItemClick {
            override fun onClick(uId: String) {
                val intent = Intent(activity, ReviewDetailActivity::class.java)
                intent.putExtra(Extra.EXTRA_REVIEW_UID, uId)
                startActivity(intent)
            }
        })

        return binding.root
    }
}