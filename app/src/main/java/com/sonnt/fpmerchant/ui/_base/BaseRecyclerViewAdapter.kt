package com.sonnt.fpmerchant.ui._base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.RecyclerView
import com.sonnt.fpmerchant.BR

open class BaseRecyclerViewAdapter<T: Any, BD: ViewDataBinding>(
    @LayoutRes val itemLayout: Int,
    var onItemClicked: (Int, T) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<BaseRecyclerViewAdapter<T, BD>.BaseViewHolder<BD>>() {

    var items: List<T> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BD> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<BD>(layoutInflater, itemLayout, parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BD>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class BaseViewHolder<BD: ViewDataBinding>(private val binding: BD): RecyclerView.ViewHolder(binding.root) {
        var item: T? = null

        fun bind(item: T) {
            this.item = item

            binding.setVariable(BR.item, item)
            binding.setVariable(BR.index, adapterPosition)
            binding.setVariable(BR.clickListener, this::onClickItem)
        }

        fun onClickItem() {
            val item = this.item ?: return
            onItemClicked(adapterPosition, item)
        }

    }

}