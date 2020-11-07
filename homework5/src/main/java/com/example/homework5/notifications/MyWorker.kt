package com.example.homework5.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.homework5.CustomTimer

class MyWorker(private val context: Context, params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
        val data = inputData.getInt(CustomTimer.BUNDLE_TIME, 0)
        NotificationsBuilder.intent.pushNotification(context, data)
        return Result.success()
    }
}