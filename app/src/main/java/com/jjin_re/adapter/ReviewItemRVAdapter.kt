package com.jjin_re.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jjin_re.databinding.RecyclerItemReviewBinding
import com.jjin_re.model.ReviewModel
import com.jjin_re.utils.BaseApplication
import com.jjin_re.utils.DiffCallback
import com.jjin_re.utils.Photo

class ReviewItemRVAdapter(private val requestManager: RequestManager, var list: List<ReviewModel> = emptyList()): RecyclerView.Adapter<ReviewItemRVAdapter.ViewHolder>() {

    private lateinit var itemClick: ItemClick

    interface ItemClick {
        fun onClick(uId: String)
    }

    fun setItemClick(itemClick: ItemClick) {
        this.itemClick = itemClick
    }

    class ViewHolder(val binding: RecyclerItemReviewBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(newData: MutableList<ReviewModel>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.list, newData))
        this.list = newData
        diffResult.dispatchUpdatesTo(this@ReviewItemRVAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecyclerItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val urlArr = list[position].imgUrl.split("[@]")

        requestManager.load(urlArr[0]).into(holder.binding.ivReviewItemThumb)

        holder.binding.tvReviewItemProductName.text = list[position].productName
        holder.binding.tvReviewItemThumbDownCnt.text = list[position].angryCnt
        holder.binding.tvReviewItemThumbUpCnt.text = list[position].likeCnt
        holder.binding.ratingReviewItem.rating = list[position].rating
        holder.binding.tvReviewItemReviewer.text = list[position].userName
        holder.binding.layoutMultiLinkPageItem.setOnClickListener {
            itemClick.onClick(list[position].uId)
        }
    }

    override fun getItemCount(): Int = list.size

}