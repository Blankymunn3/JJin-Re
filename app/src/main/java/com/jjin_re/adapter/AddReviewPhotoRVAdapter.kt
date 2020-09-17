package com.jjin_re.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jjin_re.databinding.RecyclerItemAddReviewPhotoBinding
import com.jjin_re.utils.BaseActivity
import com.jjin_re.utils.Photo

class AddReviewPhotoRVAdapter(private val requestManager: RequestManager, var list: List<Photo> = emptyList()): RecyclerView.Adapter<AddReviewPhotoRVAdapter.ViewHolder>() {

    private lateinit var deleteItemClick: DeleteItemClick

    interface DeleteItemClick {
        fun onClick(position: Int)
    }

    fun setDeleteItemClick(itemClick: DeleteItemClick) {
        this.deleteItemClick = itemClick
    }

    class ViewHolder(val binding: RecyclerItemAddReviewPhotoBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(newData: MutableList<Photo>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.list, newData))
        this.list = newData
        diffResult.dispatchUpdatesTo(this@AddReviewPhotoRVAdapter)
    }

    class DiffCallback(private val oldData: List<Photo>, private val newData: List<Photo>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldData[oldItemPosition] == newData[newItemPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecyclerItemAddReviewPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        requestManager
            .load(list[position].photo)
            .into(holder.binding.ivAddReviewPhoto)

        holder.binding.ivAddReviewPhotoDelete.setOnClickListener {
            deleteItemClick.onClick(position)
        }
    }

    override fun getItemCount() = list.size

}