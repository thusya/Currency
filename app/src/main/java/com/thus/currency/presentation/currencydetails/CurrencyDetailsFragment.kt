package com.thus.currency.presentation.currencydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.thus.core.base.fragment.BaseFragment
import com.thus.currency.databinding.CurrencyexchangeFragmentCurrencyDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyDetailsFragment : BaseFragment() {

    private lateinit var viewBinding: CurrencyexchangeFragmentCurrencyDetailsBinding

    val args: CurrencyDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = CurrencyexchangeFragmentCurrencyDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupView()
    }

    private fun setupView() {
        with(viewBinding) {
            val currencyDomain = args.currencyDomain
            tvCurrencyId.text = currencyDomain.currencyId
            tvRate.text = currencyDomain.rate.toString()
        }
    }

    private fun setupToolbar() {
        with(viewBinding) {
            (activity as AppCompatActivity?)?.run {
                setSupportActionBar(toolbar)
                supportActionBar?.let {
                    it.setDisplayHomeAsUpEnabled(true)
                    it.setDisplayShowHomeEnabled(true)
                }
                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
                setToolbarTitle(args.currencyDomain.currencyName)
            }
        }
    }

    private fun setToolbarTitle(title: String) {
        viewBinding.toolbar.title = title
        (activity as AppCompatActivity?)?.title = title
    }
}
