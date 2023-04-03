package com.thus.currency.presentation.currencyexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thus.core.base.fragment.BaseFragment
import com.thus.currency.R
import com.thus.currency.databinding.CurrencyexchangeFragmentCurrencyExchangeBinding
import com.thus.currency.domain.model.CurrencyDomain
import com.thus.currency.presentation.currencyexchange.model.OneRowItemUI
import com.thus.currency.presentation.currencyexchange.model.toOneRowItemUI
import com.thus.currency.presentation.currencyexchange.viewstate.UiState
import com.thus.currency.presentation.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyExchangeFragment : BaseFragment(), CurrencyCallbackInterface {
    private val currencyAdapter: CurrencyAdapter = CurrencyAdapter(this)

    private val viewModel: CurrencyExchangeViewModel by viewModels()

    private lateinit var viewBinding: CurrencyexchangeFragmentCurrencyExchangeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = CurrencyexchangeFragmentCurrencyExchangeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupLoadingView()
        setupBaseView()
        setupSearchView()
        setupRecyclerView()
    }

    private fun setupBaseView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.baseCurrencyState.collect { currencyDomain ->
                    with(viewBinding) {
                        tvEnd.text = currencyDomain.currencyId
                        tvStart.text = currencyDomain.currencyName
                        ivStar.setImageResource(
                            if (currencyDomain.star) {
                                R.drawable.ic_star
                            } else {
                                R.drawable.ic_unstar
                            }
                        )
                        etBaseValue.doOnTextChanged { text, _, _, _ ->
                            viewModel.setAmountChange(text.toString())
                        }
                        ivOption.setOnClickListener {
                            viewModel.setBaseCurrency(currencyDomain.currencyId)
                        }
                        ivStar.setOnClickListener {
                            if (currencyDomain.star) {
                                viewModel.removeStar(currencyDomain.currencyId)
                            } else {
                                viewModel.setStar(currencyDomain.currencyId)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupSearchView() {
        with(viewBinding) {
            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.setSearchChange(text.toString())
            }
        }
    }

    private fun setupLoadingView() {
        with(viewBinding) {
            vSwipeRefresh.setOnRefreshListener {
                vSwipeRefresh.isRefreshing = false
                viewModel.loadingData()
            }
            btnTry.setOnClickListener {
                viewModel.loadingData()
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { uiState ->
                        when (uiState) {
                            UiState.Normal -> {
                                clContent.visibility = View.VISIBLE
                                clError.visibility = View.GONE
                                vSwipeRefresh.isRefreshing = false
                            }
                            is UiState.Error -> {
                                clError.visibility = View.VISIBLE
                                clContent.visibility = View.GONE
                                vSwipeRefresh.isRefreshing = false
                                tvErrorText.text = getString(
                                    R.string.error_common_text,
                                    uiState.exception.message.toString()
                                )
                            }
                            else -> {
                                vSwipeRefresh.isRefreshing = true
                                clError.visibility = View.GONE
                                clContent.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(viewBinding.rvRecordList) {
            adapter = currencyAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currencyState.collect {
                    currencyAdapter.submitData(lifecycle, it.map { rateDomain ->
                        rateDomain.toOneRowItemUI()
                    })
                }
            }
        }
    }

    private fun setupToolbar() {
        with(viewBinding) {
            (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
            setToolbarTitle(getString(R.string.text_title_currency))
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.serverUpdatedTimestampState.collect { timestamp ->
                        val timestampFormat = Utils.dateTimeFormat(timestamp)
                        if (timestampFormat.isNotBlank()) {
                            setToolbarTitle(
                                getString(
                                    R.string.text_title_currency_with_timestamp,
                                    timestampFormat
                                )
                            )
                        } else {
                            setToolbarTitle(
                                getString(
                                    R.string.text_title_currency,
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setToolbarTitle(title: String) {
        viewBinding.toolbar.title = title
        (activity as AppCompatActivity?)?.title = title
    }

    override fun onClickOption(oneRowItemUI: OneRowItemUI) {
        (oneRowItemUI.data as? CurrencyDomain)?.let { currencyDomain ->
            viewModel.setBaseCurrency(currencyDomain.currencyId)
        }
    }

    override fun onClickStar(oneRowItemUI: OneRowItemUI) {
        (oneRowItemUI.data as? CurrencyDomain)?.let { currencyDomain ->
            if (currencyDomain.star) {
                viewModel.removeStar(currencyDomain.currencyId)
            } else {
                viewModel.setStar(currencyDomain.currencyId)
                viewBinding.rvRecordList.postDelayed(100L) {
                    viewBinding.rvRecordList.smoothScrollToPosition(0)
                }
            }
        }
    }

    override fun onClickItem(oneRowItemUI: OneRowItemUI) {
        (oneRowItemUI.data as? CurrencyDomain)?.let { currencyDomain ->
            val action =
                CurrencyExchangeFragmentDirections.actionCurrencyExchangeFragmentToCurrencyDetailsFragment(
                    currencyDomain
                )
            findNavController().navigate(action)
        }
    }
}
