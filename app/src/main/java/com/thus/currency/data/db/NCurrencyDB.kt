package com.thus.currency.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thus.currency.data.constants.Constants
import com.thus.currency.data.db.model.TbCurrency
import com.thus.currency.data.db.model.TbExchangeRate
import com.thus.currency.data.db.model.TbStar

@Database(
    entities = [TbExchangeRate::class, TbCurrency::class, TbStar::class],
    version = 1,
    exportSchema = false
)
abstract class NCurrencyDB : RoomDatabase() {
    abstract fun currencyDAO(): NCurrencyDAO

    companion object {
        fun create(context: Context): NCurrencyDB {
            return Room.databaseBuilder(context, NCurrencyDB::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
