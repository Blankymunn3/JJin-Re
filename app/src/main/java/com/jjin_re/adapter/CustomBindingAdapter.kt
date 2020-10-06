package com.jjin_re.adapter

import android.annotation.SuppressLint
import android.opengl.GLES30
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.willy.ratingbar.RotationRatingBar
import com.jjin_re.R
import com.jjin_re.utils.OutlineTextView

object CustomBindingAdapter {

    @BindingAdapter("app:profileImage", requireAll = false)
    @JvmStatic
    fun setProfileImageViewWithGlide(view: RoundedImageView, imgUri: String?) {
        imgUri?.let {
            Glide.with(view.context).load(it).placeholder(R.drawable.ic_profile_default).into(view)
        }
    }

    @BindingAdapter("app:editProfileImage", requireAll = false)
    @JvmStatic
    fun editProfileImage(view: ImageView, imgUri: String?) {
        imgUri?.let {
            Glide.with(view.context).load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((GLES30.GL_MAX_TEXTURE_SIZE * 0.4).toInt(), (GLES30.GL_MAX_TEXTURE_SIZE * 0.4).toInt())
                .transition(DrawableTransitionOptions.withCrossFade()).placeholder(R.drawable.kakao_default_profile_image).into(view)
        }
    }

    @BindingAdapter("app:userProfileSocialType")
    @JvmStatic
    fun userProfileSocialType(view: RoundedImageView, type: String?) {
        type?.let {
            when (type) {
                "kakao" -> {
                    Glide.with(view.context).load(R.drawable.kakaotalk_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(null).into(view)
                }
                "google" -> {
                    Glide.with(view.context).load(R.drawable.common_google_signin_btn_icon_dark_normal)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(null).into(view)
                }
                else -> {
                    view.isVisible = false
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @BindingAdapter("app:dateFormatSetText")
    @JvmStatic
    fun dateFormatSetText(view: TextView, date: String?) {
        date?.let {
            val changeDate = it.replace("T", " ").replace("Z", "").replace(".000", "")
            view.text = changeDate
        }
    }

    @BindingAdapter("setReviewMyThumbType")
    @JvmStatic
    fun setReviewMyThumbType(view: ImageView, type: String?) {
        type?.let {
            if (type == "0") {
                if (view.id == R.id.iv_review_detail_thumb_up) view.setColorFilter(ContextCompat.getColor(view.context, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY)
                else view.setColorFilter(ContextCompat.getColor(view.context, R.color.colorGray), android.graphics.PorterDuff.Mode.MULTIPLY)
            } else if (type == "1") {
                if (view.id == R.id.iv_review_detail_thumb_down) view.setColorFilter(ContextCompat.getColor(view.context, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY)
                else view.setColorFilter(ContextCompat.getColor(view.context, R.color.colorGray), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    @BindingAdapter("setRating")
    @JvmStatic
    fun setRating(view: RotationRatingBar, rating: Float) {
        view.rating = rating
    }

    @BindingAdapter("setCategory")
    @JvmStatic
    fun setCategory(view: TextView, category: String?) {
        category?.let {
            view.text = view.context.resources.getStringArray(R.array.str_review_category)[it.toInt()]
        }
    }

    @BindingAdapter("setRecyclerItem")
    @JvmStatic
    fun setRecyclerItem(view: ShimmerRecyclerView, list: MutableList<String>) {
        val recyclerViewAdapter = ReviewThumbnailRVAdapter(Glide.with(view.context))
        recyclerViewAdapter.setData(list)
    }

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter("setWebView")
    @JvmStatic
    fun setWebView(view: WebView, url: String?) {
        url?.let {
            view.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url!!)
                    return true
                }
            }
            view.loadUrl(it)
            view.settings.setSupportZoom(true)
            view.settings.defaultZoom = WebSettings.ZoomDensity.FAR
            view.settings.javaScriptEnabled = true
            view.settings.useWideViewPort = true
            view.settings.loadWithOverviewMode = true
            view.settings.domStorageEnabled = true
        }
    }
}