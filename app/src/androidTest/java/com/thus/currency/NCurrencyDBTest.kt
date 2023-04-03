package com.thus.currency

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.thus.currency.data.db.NCurrencyDB
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

abstract class NCurrencyDBTest {
    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()
    private lateinit var _dbN: NCurrencyDB
    val dbN: NCurrencyDB
        get() = _dbN

    @Before
    fun initDb() {
        _dbN = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NCurrencyDB::class.java
        ).build()
    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        _dbN.close()
    }
}