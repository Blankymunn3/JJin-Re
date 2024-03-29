package com.jjin_re.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jjin_re.R
import com.jjin_re.databinding.RecyclerItemReviewThumbnailBinding
import com.jjin_re.utils.DiffCallback


class ReviewThumbnailRVAdapter(
    private val requestManager: RequestManager,
    private var reviewThumbnailList: List<String> = emptyList()
): RecyclerView.Adapter<ReviewThumbnailRVAdapter.ViewHolder>() {

    private lateinit var itemClick: ItemClick

    interface ItemClick {
        fun onClick(imageUri: ArrayList<String>)
    }

    fun setItemClick(itemClick: ItemClick) {
        this.itemClick = itemClick
    }

    class ViewHolder(val binding: RecyclerItemReviewThumbnailBinding): RecyclerView.ViewHolder(
        binding.root
    )

    fun setData(newData: MutableList<String>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.reviewThumbnailList, newData))
        this.reviewThumbnailList = newData
        diffResult.dispatchUpdatesTo(this@ReviewThumbnailRVAdapter)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerItemReviewThumbnailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        requestManager.load(reviewThumbnailList[position]).into(holder.binding.ivReviewThumbnail)

        val mIcon = holder.binding.ivReviewThumbnail
        val bitmap = BitmapFactory.decodeResource(holder.binding.root.resources, R.drawable.ic_arr_back_white)
        val mDrawable = RoundedBitmapDrawableFactory.create(holder.binding.root.resources, bitmap)
        mDrawable.isCircular = true
        mIcon.setImageDrawable(mDrawable)
        holder.binding.ivReviewThumbnail.setOnClickListener {
            val arr: ArrayList<String> = ArrayList(emptyList())
            arr.addAll(reviewThumbnailList)
            itemClick.onClick(arr)
        }
    }

    override fun getItemCount(): Int = reviewThumbnailList.size

}