package io.kim_kong.jjin_re.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.makeramen.roundedimageview.RoundedImageView
import com.willy.ratingbar.RotationRatingBar
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.utils.OutlineTextView

object CustomBindingAdapter {

    @BindingAdapter("app:profileImage", requireAll = false)
    @JvmStatic
    fun setProfileImageViewWithGlide(view: RoundedImageView, imgUri: String?) {
        imgUri?.let {
            Glide.with(view.context).load(it).placeholder(R.drawable.ic_profile_default).into(view)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @BindingAdapter("app:dateFormatSetText")
    @JvmStatic
    fun dateFormatSetText(view: OutlineTextView, date: String?) {
        date?.let {
            val changeDate = date.replace("T", " ").replace("Z", "").replace(".000", "")

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