package com.example.cfthomework

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log

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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(task)
    }
}