package com.jjin_re.features.review_detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.GLES30
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
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
import com.jjin_re.features.edit_review.EditReviewActivity
import com.jjin_re.utils.*
import com.jjin_re.utils.Extra.EXTRA_REVIEW_UID
import me.relex.circleindicator.CircleIndicator2
import kotlin.collections.ArrayList

class ReviewDetailActivity : BaseActivity() {
    val binding by binding<ActivityReviewDetailBinding>(R.layout.activity_review_detail)
    val viewModel by GetViewModel(ReviewDetailViewModel::class.java)
    private lateinit var reviewThumbnailRVAdapter: ReviewThumbnailRVAdapter
    private lateinit var snapHelper: SnapHelperOneByOne
    private lateinit var modifiedItem: MenuItem
    private lateinit var removeItem: MenuItem

    lateinit var requestManager: RequestManager
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_review, menu)
        modifiedItem = menu.findItem(R.id.menu_modified)
        removeItem = menu.findItem(R.id.menu_remove)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_modified -> {
                val intent = Intent(this@ReviewDetailActivity, EditReviewActivity::class.java)
                intent.putExtra("EXTRA_REVIEW", viewModel.responseBody.value)
                ActivityCompat.startActivity(this@ReviewDetailActivity, intent, null)
                true
            }
            R.id.menu_remove -> {
                viewModel.removeReview()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@ReviewDetailActivity)
        binding.lifecycleOwner = this@ReviewDetailActivity
        binding.viewModel = viewModel

        setSupportActionBar(binding.tbReviewDetail)

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

        viewModel.responseBody.observe(this@ReviewDetailActivity, {
            if (BaseApplication.userModel.userId == it.userId) {
                removeItem.isVisible = true
                modifiedItem.isVisible = true
            } else {
                removeItem.isVisible = false
                modifiedItem.isVisible = false
            }
        })

        viewModel.urlArr.observe(this@ReviewDetailActivity, {
            if (it.isNotEmpty()) {
                reviewThumbnailRVAdapter.setData(it as MutableList<String>)
                val circleIndicator2 : CircleIndicator2 = binding.indicator
                circleIndicator2.attachToRecyclerView(binding.rvReviewDetailThumbnail, snapHelper)
                viewModel.getReviewMyThumb()
            }
        })

        viewModel.uId.observe(this@ReviewDetailActivity, {
            if (!it.isNullOrEmpty()) viewModel.getReviewDetailDataFromServer()
        })

        viewModel.responseCode.observe(this@ReviewDetailActivity, {
            if (it.isNotEmpty() && it == "200") {
                Utils.showToast(viewModel.responseMessage.value!!, this@ReviewDetailActivity)
                finish()
            }
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

    override fun onPause() {
        super.onPause()
        actList.remove(this@ReviewDetailActivity)
    }
}