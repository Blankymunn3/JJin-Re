package com.jjin_re.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jjin_re.databinding.RecyclerItemAddReviewPhotoBinding
import com.jjin_re.utils.DiffCallback
import com.jjin_re.utils.Photo

class AddReviewPhotoRVAdapter(private val requestManager: RequestManager, private var listPhoto: List<Photo> = emptyList()) : RecyclerView.Adapter<AddReviewPhotoRVAdapter.ViewHolder>() {

    private lateinit var itemClick: OnItemClick

    interface OnItemClick {
        fun onDeleteClick(position: Int)
        fun onAddClick(position: Int)
    }

    fun setItemClick(itemClick: OnItemClick) {
        this.itemClick = itemClick
    }


    class ViewHolder(val binding: RecyclerItemAddReviewPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setPhotoData(newData: MutableList<Photo>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.listPhoto, newData))
        this.listPhoto = newData
        diffResult.dispatchUpdatesTo(this@AddReviewPhotoRVAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecyclerItemAddReviewPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.layoutAddedPhotoItem.visibility = View.GONE
            holder.binding.ivAddPhotoItem.visibility = View.VISIBLE
            holder.binding.ivAddPhotoItem.setOnClickListener {
                itemClick.onAddClick(position)
            }
        } else {
            holder.binding.layoutAddedPhotoItem.visibility = View.VISIBLE
            holder.binding.ivAddPhotoItem.visibility = View.GONE
            requestManager
                .load(listPhoto[position].photo)
                .into(holder.binding.ivAddReviewPhoto)

            holder.binding.ivAddReviewPhotoDelete.setOnClickListener {
                itemClick.onDeleteClick(position)
            }
        }

    }

    override fun getItemCount() = listPhoto.size

}