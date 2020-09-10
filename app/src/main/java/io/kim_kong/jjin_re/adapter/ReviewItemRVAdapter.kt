package io.kim_kong.jjin_re.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import io.kim_kong.jjin_re.databinding.RecyclerItemReviewBinding
import io.kim_kong.jjin_re.model.ReviewModel
import io.kim_kong.jjin_re.utils.Photo

class ReviewItemRVAdapter(private val requestManager: RequestManager, var list: List<ReviewModel> = emptyList()): RecyclerView.Adapter<ReviewItemRVAdapter.ViewHolder>() {

    class ViewHolder(val binding: RecyclerItemReviewBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(newData: MutableList<ReviewModel>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.list, newData))
        this.list = newData
        diffResult.dispatchUpdatesTo(this@ReviewItemRVAdapter)
    }

    class DiffCallback(private val oldData: List<ReviewModel>, private val newData: List<ReviewModel>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldData[oldItemPosition] == newData[newItemPosition]
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
        holder.binding.ratingReviewItem.rating = list[position].rating.toFloat()
        holder.binding.tvReviewItemReviewer.text = list[position].userName
    }

    override fun getItemCount(): Int = list.size

}