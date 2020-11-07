package com.example.homework5.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.homework5.CustomTimer

class ReminderReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            val clockTimes = intent?.getIntExtra(CustomTimer.BUNDLE_TIME, 0)
            NotificationsBuilder.intent.pushNotification(it, clockTimes?: 0)
        }
    }
}