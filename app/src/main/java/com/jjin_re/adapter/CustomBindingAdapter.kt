package com.jjin_re.adapter

import android.annotation.SuppressLint
import android.opengl.GLES30
import android.widget.ImageView
import android.widget.TextView
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
                .transition(DrawableTransitionOptions.withCrossFade()).placeholder(R.drawable.ic_profile_default).into(view)
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
}