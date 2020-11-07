package com.example.homework5.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.homework5.R

class NotificationsBuilder private constructor(){

    companion object {
        val intent = NotificationsBuilder()
        var counter = 0
    }

    private val channelName = "default"
    private val channelId = "0"
    private var notificationManager: NotificationManager? = null

    private fun createNotificationsChannel(id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager?.createNotificationChannel(channel)
    }

    fun pushNotification(context: Context, clockTimes: Int) {
        if (notificationManager == null) {
            notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationsChannel(channelId, channelName)
        }

        val notification =
                NotificationCompat.Builder(context, channelId)
                    .setContentText("Timer counter was $clockTimes")
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .build()
        notificationManager?.notify(counter++, notification)

    }
}