package com.jjin_re.features.review_detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.GLES30
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.ads.*
import com.stfalcon.imageviewer.StfalconImageViewer
import com.jjin_re.R
import com.jjin_re.adapter.ReviewThumbnailRVAdapter
import com.jjin_re.databinding.ActivityReviewDetailBinding
import com.jjin_re.utils.*
import com.jjin_re.utils.Extra.EXTRA_REVIEW_UID
import me.relex.circleindicator.CircleIndicator2
import kotlin.collections.ArrayList

class ReviewDetailActivity : BaseActivity() {
    val binding by binding<ActivityReviewDetailBinding>(R.layout.activity_review_detail)
    val viewModel by GetViewModel(ReviewDetailViewModel::class.java)
    private lateinit var reviewThumbnailRVAdapter: ReviewThumbnailRVAdapter
    private lateinit var snapHelper :SnapHelperOneByOne

    lateinit var requestManager : RequestManager

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
            override fun onClick(imageUri: ArrayList<String>) {

                StfalconImageViewer.Builder(this@ReviewDetailActivity, ArrayList(imageUri)) { view, image ->
                    requestManager
                        .load(image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(
                            (GLES30.GL_MAX_TEXTURE_SIZE * 0.4).toInt(),
                            (GLES30.GL_MAX_TEXTURE_SIZE * 0.4).toInt()
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(ColorDrawable(Color.GRAY))
                        .into(view)
                }.show()
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

    fun onClickReviewThumbUpAndThumbDown(view: View) {
        when (view.id) {
            R.id.iv_review_detail_thumb_up -> {

            }
            R.id.iv_review_detail_thumb_down -> {

            }
        }
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@ReviewDetailActivity)
    }
}