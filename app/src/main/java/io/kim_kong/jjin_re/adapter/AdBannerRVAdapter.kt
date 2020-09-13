package io.kim_kong.jjin_re.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import io.kim_kong.jjin_re.databinding.RecyclerItemAdBannerBinding
import io.kim_kong.jjin_re.model.AdBannerModel


class AdBannerRVAdapter(private val requestManager: RequestManager, var list :List<AdBannerModel> = emptyList()): RecyclerView.Adapter<AdBannerRVAdapter.ViewHolder>(){
   class ViewHolder(val binding: RecyclerItemAdBannerBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(newData: MutableList<AdBannerModel>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.list, newData))
        this.list = newData
        diffResult.dispatchUpdatesTo(this@AdBannerRVAdapter)
    }

    class DiffCallback(private val oldData: List<AdBannerModel>, private val newData: List<AdBannerModel>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldData[oldItemPosition] == newData[newItemPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemAdBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        requestManager.load(list[position].imgUri).into(holder.binding.rivAdBanner)
        holder.binding.tvAdBannerCnt.text = "${position + 1} / ${list.size} +"
    }

    override fun getItemCount(): Int = list.size
}