package com.thus.currency.presentation.currencyexchange

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thus.currency.presentation.currencyexchange.model.OneRowItemUI
import com.thus.currency.presentation.currencyexchange.viewholder.OneRowItemViewHolder

class CurrencyAdapter(private var currencyCallbackInterface: CurrencyCallbackInterface? = null) :
    PagingDataAdapter<OneRowItemUI, RecyclerView.ViewHolder>(itemDiffCallback) {

    companion object {
        val itemDiffCallback = object : DiffUtil.ItemCallback<OneRowItemUI>() {
            override fun areItemsTheSame(
                oldItem: OneRowItemUI,
                newItem: OneRowItemUI
            ): Boolean {
                return oldItem.endText == newItem.endText
            }
            override fun areContentsTheSame(
                oldItem: OneRowItemUI,
                newItem: OneRowItemUI
            ): Boolean {
                return oldItem == newItem
            }
        }
        const val TYPE_CURRENCY = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_CURRENCY -> OneRowItemViewHolder.create(parent, currencyCallbackInterface)
            else -> OneRowItemViewHolder.create(parent, currencyCallbackInterface)
        }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is OneRowItemUI -> TYPE_CURRENCY
        else -> TYPE_CURRENCY
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItem(position)
        when (holder) {
            is OneRowItemViewHolder -> if (type is OneRowItemUI) holder.bindView(
                type
            )
        }
    }
}

interface CurrencyCallbackInterface {
    fun onClickOption(oneRowItemUI: OneRowItemUI)
    fun onClickStar(oneRowItemUI: OneRowItemUI)
    fun onClickItem(oneRowItemUI: OneRowItemUI)
}