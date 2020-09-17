package com.jjin_re.features.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jjin_re.R
import com.jjin_re.adapter.FragmentAdapter
import com.jjin_re.adapter.ReviewItemRVAdapter
import com.jjin_re.databinding.ActivitySearchBinding
import com.jjin_re.features.category.category_fragment.CategoryFragment
import com.jjin_re.features.review_detail.ReviewDetailActivity
import com.jjin_re.utils.*

class SearchActivity:BaseActivity() {
    val binding by binding<ActivitySearchBinding>(R.layout.activity_search)
    val viewModel by GetViewModel(SearchViewModel::class.java)
    private lateinit var requestManager: RequestManager
    private lateinit var linearSnapHelper: LinearSnapHelper
    private lateinit var reviewItemRVAdapter: ReviewItemRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@SearchActivity)
        Utils.setIconTintDark(this@SearchActivity, true)
        binding.lifecycleOwner = this@SearchActivity
        binding.viewModel = viewModel
        requestManager = Glide.with(this@SearchActivity)
        linearSnapHelper = SnapHelperOneByOne()
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (viewModel.reviewList.value!!.size >= 20) {
                    viewModel.page.postValue(viewModel.page.value!!.plus(1))
                    viewModel.searchReviewListDownloadFromServer()
                }
            }
        }
        binding.rvReviewItem.addOnScrollListener(scrollListener)

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY) ?.also { query ->
                SearchRecentSuggestions(
                    this@SearchActivity,
                    MySuggestionProvider.AUTHORITY,
                    MySuggestionProvider.MODE
                )
                    .saveRecentQuery(query, null)
            }
        }
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchKeyword.postValue(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchKeyword.postValue(newText)
                return false
            }
        })


        reviewItemRVAdapter = ReviewItemRVAdapter(requestManager)
        binding.rvReviewItem.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            this.layoutManager = layoutManager
            adapter = reviewItemRVAdapter
        }

        viewModel.searchKeyword.observe(this@SearchActivity, {
            if (!it.isNullOrEmpty()) viewModel.searchReviewListDownloadFromServer()
        })


        viewModel.reviewList.observe(this@SearchActivity, {
            binding.rvReviewItem.hideShimmerAdapter()
            reviewItemRVAdapter.setData(it)
            if (it.size > 0) {
                binding.tvReviewListEmpty.visibility = View.GONE
                binding.lottieEmptyReview.visibility = View.GONE
                binding.lottieEmptyReview.loop(false)
            } else {
                binding.tvReviewListEmpty.visibility = View.VISIBLE
                binding.lottieEmptyReview.visibility = View.VISIBLE
                binding.lottieEmptyReview.playAnimation()
                binding.lottieEmptyReview.loop(true)
            }
        })

        reviewItemRVAdapter.setItemClick(object : ReviewItemRVAdapter.ItemClick {
            override fun onClick(uId: String) {
                val intent = Intent(this@SearchActivity, ReviewDetailActivity::class.java)
                intent.putExtra(Extra.EXTRA_REVIEW_UID, uId)
                startActivity(intent)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@SearchActivity)
    }
}