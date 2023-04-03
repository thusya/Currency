package com.thus.currency.presentation.currencyexchange.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thus.currency.R
import com.thus.currency.databinding.CurrencyexchangeItemOneRowBinding
import com.thus.currency.presentation.currencyexchange.CurrencyCallbackInterface
import com.thus.currency.presentation.currencyexchange.model.OneRowItemUI

class OneRowItemViewHolder(
    private val currencyCallbackInterface: CurrencyCallbackInterface? = null,
    private val binding: CurrencyexchangeItemOneRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(
        oneRowItemUI: OneRowItemUI
    ) {
        with(binding) {
            tvStart.text = oneRowItemUI.startText
            tvMiddle.text = oneRowItemUI.middleText
            tvEnd.text = oneRowItemUI.endText
            ivStar.setImageResource(
                if (oneRowItemUI.star) {
                    R.drawable.ic_star
                } else {
                    R.drawable.ic_unstar
                }
            )
            ivOption.setOnClickListener {
                currencyCallbackInterface?.onClickOption(oneRowItemUI)
            }
            ivStar.setOnClickListener {
                currencyCallbackInterface?.onClickStar(oneRowItemUI)
            }
            root.setOnClickListener {
                currencyCallbackInterface?.onClickItem(oneRowItemUI)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            currencyCallbackInterface: CurrencyCallbackInterface? = null
        ): OneRowItemViewHolder {
            return OneRowItemViewHolder(
                currencyCallbackInterface,
                CurrencyexchangeItemOneRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}