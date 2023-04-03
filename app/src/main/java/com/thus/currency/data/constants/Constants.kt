package com.thus.currency.data.constants

import androidx.paging.PagingConfig

object Constants {
    const val DB_NAME = "Currency.db"
    const val TB_EXCHANGE_RATE = "tb_exchange_rate"
    const val TB_CURRENCY = "tb_currency"
    const val TB_STAR = "tb_star"

    const val PREF_FILE_NAME = "pref_currency"
    const val PREF_KEY_TIMESTAMP = "pref_key_timestamp"
    const val PREF_KEY_BASE_CURRENCY = "pref_key_base_currency"

    val PAGING_CONFIG = PagingConfig(
        pageSize = 10,
        prefetchDistance = 3
    )
}