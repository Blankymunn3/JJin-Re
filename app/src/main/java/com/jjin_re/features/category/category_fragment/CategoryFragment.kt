package com.jjin_re.features.category.category_fragment

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
import com.jjin_re.databinding.FragmentCategoryBinding
import com.jjin_re.features.add_review.AddReviewActivity
import com.jjin_re.features.category.CategoryMainActivity
import com.jjin_re.features.review_detail.ReviewDetailActivity
import com.jjin_re.model.ReviewModel
import com.jjin_re.utils.*

class CategoryFragment:Fragment() {
    private lateinit var activity: CategoryMainActivity
    private val viewModel by GetViewModel(CategoryFragmentViewModel::class.java)
    private lateinit var requestManager: RequestManager
    private lateinit var linearSnapHelper: LinearSnapHelper
    private lateinit var reviewItemRVAdapter: ReviewItemRVAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as CategoryMainActivity
    }

    companion object {
        @JvmStatic
        val EXTRA_CATEGORY_DATA = "EXTRA_CATEGORY_DATA"
        @JvmStatic
        fun newInstance(bundle: Bundle, category: String): CategoryFragment {
            val fragment = CategoryFragment()
            bundle.putString(EXTRA_CATEGORY_DATA, category)
            fragment.arguments = bundle
            return fragment
        }
        @JvmStatic
        fun newInstance(): CategoryFragment = CategoryFragment()
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCategoryBinding.bind(LayoutInflater.from(context).inflate(R.layout.fragment_category, null, false))
        binding.rvReviewItem.showShimmerAdapter()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        requestManager = Glide.with(this@CategoryFragment)
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

        viewModel.category.observe(this@CategoryFragment, {
            if (!it.isNullOrEmpty())
                viewModel.reviewListDownloadFromServer()
        })

        viewModel.reviewList.observe(this@CategoryFragment, {
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