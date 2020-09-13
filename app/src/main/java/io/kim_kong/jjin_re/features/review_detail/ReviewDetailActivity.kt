package io.kim_kong.jjin_re.features.review_detail

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.gms.ads.*
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.ReviewThumbnailRVAdapter
import io.kim_kong.jjin_re.databinding.ActivityReviewDetailBinding
import io.kim_kong.jjin_re.features.photo_view.PhotoViewDialog
import io.kim_kong.jjin_re.utils.*
import io.kim_kong.jjin_re.utils.Extra.EXTRA_REVIEW_UID
import me.relex.circleindicator.CircleIndicator2
import java.util.*

class ReviewDetailActivity : BaseActivity() {
    val binding by binding<ActivityReviewDetailBinding>(R.layout.activity_review_detail)
    val viewModel by GetViewModel(ReviewDetailViewModel::class.java)
    private lateinit var reviewThumbnailRVAdapter: ReviewThumbnailRVAdapter
    private lateinit var snapHelper :SnapHelperOneByOne

    lateinit var requestManager : RequestManager

    private val photoViewDialog
        get() = supportFragmentManager.findFragmentByTag("photoDialog") as? PhotoViewDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@ReviewDetailActivity)
        binding.lifecycleOwner = this@ReviewDetailActivity
        binding.viewModel = viewModel


        binding.adViewReviewDetail.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.i(this@ReviewDetailActivity::class.java.simpleName, "onAdLoad")
            }
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                Log.e(this@ReviewDetailActivity::class.java.simpleName, "onAdFailedToLoad")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.i(this@ReviewDetailActivity::class.java.simpleName, "onAdOpened")
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.i(this@ReviewDetailActivity::class.java.simpleName, "onAdClicked")
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                Log.i(this@ReviewDetailActivity::class.java.simpleName, "onAdLeftApplication")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.i(this@ReviewDetailActivity::class.java.simpleName, "onAdClosed")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.i(this@ReviewDetailActivity::class.java.simpleName, "onAdImpression")
            }
        }

        viewModel.uId.postValue(intent.getStringExtra(EXTRA_REVIEW_UID))

        requestManager = Glide.with(this@ReviewDetailActivity)
        reviewThumbnailRVAdapter = ReviewThumbnailRVAdapter(requestManager)

        binding.rvReviewDetailThumbnail.apply {
            snapHelper = SnapHelperOneByOne()
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = SpeedyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = reviewThumbnailRVAdapter
            snapHelper.attachToRecyclerView(binding.rvReviewDetailThumbnail)
        }

        reviewThumbnailRVAdapter.setItemClick(object : ReviewThumbnailRVAdapter.ItemClick {
            override fun onClick(imageUri: List<String>) {
                photoViewDialog ?: PhotoViewDialog(requestManager, imageUri).show(supportFragmentManager, "photoDialog")
            }

        })

        viewModel.urlArr.observe(this@ReviewDetailActivity, {
            if (it.isNotEmpty()) {
                reviewThumbnailRVAdapter.setData(it as MutableList<String>)
                val circleIndicator2 : CircleIndicator2 = binding.indicator
                circleIndicator2.attachToRecyclerView(binding.rvReviewDetailThumbnail, snapHelper)
            }
        })

        viewModel.uId.observe(this@ReviewDetailActivity, {
            if (!it.isNullOrEmpty()) viewModel.getReviewDetailDataFromServer()
        })

        MobileAds.initialize(this@ReviewDetailActivity, getString(R.string.ad_mob_id))
        val adRequest = AdRequest.Builder().build()
        binding.adViewReviewDetail.loadAd(adRequest)

        /* -- test ad mob --
        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        */
    }
}