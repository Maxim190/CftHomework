package com.example.cfthomework

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService: Service() {

    companion object{
        val BROADCAST_ACTION = "my.action.service_sms"
        val MSG_NAME = "service_msg"
    }

    private val handler: Handler = Handler()
    private val task =
        Runnable {
            val intent = Intent(BROADCAST_ACTION)
            intent.putExtra(MSG_NAME, "Hello from service")
            sendBroadcast(intent)
        }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.postDelayed(task, 2000)
        return START_NOT_STICKY
    }

    private fun runNotification() {
        val notification= NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Hello!")
                .setContentTitle("My first notification")
                .build()
        val manager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
        manager?.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(task)
    }
}