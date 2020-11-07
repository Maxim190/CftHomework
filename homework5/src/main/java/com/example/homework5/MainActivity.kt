package com.example.homework5

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.homework5.notifications.MyWorker
import com.example.homework5.notifications.ReminderReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), CustomTimer.TimeListener {

    private var timer: CustomTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timer = CustomTimer(this)
        Thread {
            timer?.start()
        }.start()

    }

    override fun onStop() {
        sendDelayedNotificationWithAlarmManager()
        super.onStop()
    }

    private fun sendDelayedNotificationWithAlarmManager() {
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra(CustomTimer.BUNDLE_TIME, timer?.counter)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10000L,
                pendingIntent)
    }

    private fun sendDelayedNotificationWithWorkManager() {
        val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(10, TimeUnit.SECONDS)

        val data = timer?.counter?.let {
            Data.Builder()
                    .putInt(CustomTimer.BUNDLE_TIME, it)
                    .build()
        }
        data?.let {
            request.setInputData(data)
        }
        WorkManager.getInstance(baseContext).enqueue(request.build())
    }

    override fun onTick(seconds: Int) {
        textView.text = seconds.toString()
    }
}