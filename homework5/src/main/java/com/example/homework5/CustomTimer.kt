package com.example.homework5

import android.os.Handler
import android.os.Looper
import android.util.Log

class CustomTimer(private val listener: TimeListener? = null){

    companion object {
        const val BUNDLE_TIME = "time"
    }

    interface TimeListener {
        fun onTick(seconds: Int)
    }

    private var isStopped = false
    private var isRunning = false

    var counter = 0
        private set

    private fun loop() {
        val handler = Handler(Looper.getMainLooper())
        while(!isStopped) {
            isRunning = true
            handler.post {
                listener?.onTick(counter)
            }
            Thread.sleep(1000L)
            counter++
        }
        isRunning = false
    }

    fun start() {
        if (isRunning) {
            return
        }
        isStopped = false
        loop()
    }

    fun stop() {
        isStopped = true
    }
}