package org.redesnac.lsgbible

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BibleApp: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()


    override fun onCreate() {
        super.onCreate()

        val myConfig = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

        WorkManager.initialize(this, myConfig)

    }
}