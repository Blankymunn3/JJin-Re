package io.kim_kong.jjin_re.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import io.kim_kong.jjin_re.databinding.RecyclerItemPhotoBinding

class PhotoRVAdapter(private val requestManager: RequestManager, var photoList: List<String> = emptyList()): RecyclerView.Adapter<PhotoRVAdapter.ViewHolder>() {
    class ViewHolder(val binding: RecyclerItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(newData: List<String>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.photoList, newData))
        this.photoList = newData
        diffResult.dispatchUpdatesTo(this@PhotoRVAdapter)
    }

    class DiffCallback(private val oldData: List<String>, private val newData: List<String>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldData[oldItemPosition] == newData[newItemPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemPhotoBinding.inflate(LayoutInflater.from(parent.context), null, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        requestManager.load(photoList[position]).into(holder.binding.pvReviewThumbnail)
    }

    override fun getItemCount(): Int = photoList.size

}