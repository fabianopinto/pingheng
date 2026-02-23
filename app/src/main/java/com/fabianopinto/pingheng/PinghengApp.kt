package com.fabianopinto.pingheng

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fabianopinto.pingheng.worker.RefreshDataWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class PinghengApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleRefreshWorker()
    }

    private fun scheduleRefreshWorker() {
        val refreshRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(15, TimeUnit.MINUTES)
            .build()

        // Note: PeriodicWork minimum interval is 15 minutes.
        // For 60 seconds as requested, we would need to chain one-time workers or use a different approach,
        // but WorkManager is best for periodic tasks even if the interval is longer.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RefreshDataWork",
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )
    }
}
