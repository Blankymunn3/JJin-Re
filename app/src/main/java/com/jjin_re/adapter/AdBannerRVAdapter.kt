package com.jjin_re.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jjin_re.databinding.RecyclerItemAdBannerBinding
import com.jjin_re.model.AdBannerModel
import com.jjin_re.utils.DiffCallback


class AdBannerRVAdapter(private val requestManager: RequestManager, var list :List<AdBannerModel> = emptyList()): RecyclerView.Adapter<AdBannerRVAdapter.ViewHolder>(){
    private lateinit var itemClick: OnItemClick

    interface OnItemClick {
        fun itemClick(url: String)
    }

    fun setItemClick(onItemClick: OnItemClick) {
        this.itemClick = onItemClick
    }

    class ViewHolder(val binding: RecyclerItemAdBannerBinding): RecyclerView.ViewHolder(binding.root)

    fun setData(newData: MutableList<AdBannerModel>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                this.list,
                newData
            )
        )

        this.list = newData
        diffResult.dispatchUpdatesTo(this@AdBannerRVAdapter)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerItemAdBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        requestManager.load(list[position].imgUri).into(holder.binding.rivAdBanner)
        holder.binding.tvAdBannerCnt.text = "${position + 1} / ${list.size} +"
        holder.binding.rivAdBanner.setOnClickListener {
            itemClick.itemClick(list[position].url)
        }
    }

    override fun getItemCount(): Int = list.size
}