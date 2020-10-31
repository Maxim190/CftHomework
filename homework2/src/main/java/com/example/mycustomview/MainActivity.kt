package com.example.mycustomview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedometerViewAnimation = SpeedometerViewAnimation(activity_main_speedometerView)

        activity_main_gasBtn.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN ->
                    speedometerViewAnimation.speedIncAnimationStart()
                MotionEvent.ACTION_UP ->
                    speedometerViewAnimation.speedDecAnimationStart()
            }
            v.performClick()
        }
    }
}