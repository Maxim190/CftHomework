package com.example.mycustomview

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

class SpeedometerViewAnimation(private val speedometerView: SpeedometerView) {

    private val maxSpeedColor = Color.parseColor("#ffcccb")
    private val colorAnimator = ValueAnimator().apply {
        addUpdateListener {
            speedometerView.setPointerColor(it.animatedValue as Int)
            setEvaluator(ArgbEvaluator())
        }
    }
    private val speedAnimator = ValueAnimator().apply {
        addUpdateListener {
            speedometerView.setCurrentSpeed(it.animatedValue as Float)
        }
    }
    private val animatorSet = AnimatorSet().apply {
        play(speedAnimator).with(colorAnimator)
        duration = 6000L
    }
    private val fastOutSlowInInterpolator = FastOutSlowInInterpolator()
    private val linearOutSlowInInterpolator = LinearOutSlowInInterpolator()
    private var isSpeedIncAnimationRunning = false
    private val defaultPointerColor = speedometerView.pointerColor

    fun speedIncAnimationStart() {
        if (isSpeedIncAnimationRunning) {
            return
        }
        isSpeedIncAnimationRunning = true
        animatorSet.interpolator = fastOutSlowInInterpolator
        animate(
            speedFrom = speedometerView.curSpeed,
            speedTo = speedometerView.maxSpeed,
            colorFrom = speedometerView.pointerColor,
            colorTo = Color.RED)
    }

    fun speedDecAnimationStart() {
        if (!isSpeedIncAnimationRunning) {
            return
        }
        isSpeedIncAnimationRunning = false
        animatorSet.interpolator = linearOutSlowInInterpolator
        animate(
            speedFrom = speedometerView.curSpeed,
            speedTo = 0F,
            colorFrom = speedometerView.pointerColor,
            colorTo = defaultPointerColor)
    }

    private fun animate(speedFrom: Float, speedTo: Float, colorFrom: Int, colorTo: Int) {
        speedAnimator.setFloatValues(speedFrom, speedTo)
        colorAnimator.setIntValues(colorFrom, colorTo)
        animatorSet.apply {
            cancel()
            start()
        }
    }
}