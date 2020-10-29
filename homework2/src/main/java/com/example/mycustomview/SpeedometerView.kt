package com.example.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class SpeedometerView(context: Context, attrs: AttributeSet?): View(context, attrs) {

    private companion object {
        const val STATE_DIGIT_COLOR = "digitColor"
        const val STATE_MAX_SPEED = "maxSpeed"
        const val STATE_CURRENT_SPEED = "currentSpeed"
        const val STATE_SEGMENTS_AMOUNT = "segmentsAmount"
        const val STATE_POINTER_COLOR = "pointerColor"
        const val STATE_SEGMENT_COLOR = "segmentColor"
        const val STATE_SUPER = "superState"
    }

    private val DEFAULT_MAX_SPEED = 200
    private val DEFAULT_CURRENT_SPEED = 0
    private val DEFAULT_SEGMENT_COLOR = Color.CYAN
    private val DEFAULT_DIGIT_COLOR = Color.BLUE
    private val DEFAULT_POINTER_COLOR = Color.RED
    private val DEFAULT_SEGMENT_AMOUNT = 5
    private val DEFAULT_WIDTH = 250
    private val DEFAULT_HEIGHT = 250

    private var maxSpeed: Int = DEFAULT_MAX_SPEED
        set(value) {
            field = (value / blockOfSegmentsAmount) * blockOfSegmentsAmount
        }
    private var curSpeed: Int = DEFAULT_CURRENT_SPEED
        set(value) {
            field = value.coerceAtMost(maxSpeed)
        }
    private var speedStep: Int

    private var segmentColor: Int
    private var digitColor: Int
    private var pointerColor: Int

    private var blockOfSegmentsAmount: Int
    private var segmentsPerBlockAmount = 9
    private val segmentsDegreeStep: Float
    private val bigSegmentLength = 18F
    private val smallSegmentLength = 10F

    private val paint = Paint()
    private val boldStrokeWidth = 5F
    private val normalStrokeWidth = 3F

    private var arcRadius: Float = (width / 2F)
    private val paddingFromArc = 15F
    private val arcRectF = RectF()

    init {
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.SpeedometerView)
        attrsArray.apply {
            blockOfSegmentsAmount = getInteger(R.styleable.SpeedometerView_segmentationAmount, DEFAULT_SEGMENT_AMOUNT)
            maxSpeed = getInteger(R.styleable.SpeedometerView_maxSpeed, DEFAULT_MAX_SPEED)
            segmentColor = getColor(R.styleable.SpeedometerView_segmentationColor, DEFAULT_SEGMENT_COLOR)
            digitColor = getColor(R.styleable.SpeedometerView_digitColor, DEFAULT_DIGIT_COLOR)
            curSpeed = getInteger(R.styleable.SpeedometerView_currentSpeed, DEFAULT_CURRENT_SPEED)
            pointerColor = getInteger(R.styleable.SpeedometerView_pointerColor, DEFAULT_POINTER_COLOR)
        }
        segmentsDegreeStep = 180F / (segmentsPerBlockAmount * blockOfSegmentsAmount)
        speedStep = maxSpeed / blockOfSegmentsAmount

        paint.textSize = 30F
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = normalStrokeWidth

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measureDimension(DEFAULT_WIDTH, widthMeasureSpec)
        val height = measureDimension(DEFAULT_HEIGHT, widthMeasureSpec)

        val size = min(width, height)

        arcRectF.apply {
            left = paint.strokeWidth
            right = size.toFloat() - paint.strokeWidth
            bottom = size.toFloat()
            top = paint.strokeWidth
        }
        arcRadius = size / 2F

        setMeasuredDimension(size, size / 2 + paint.strokeWidth.toInt())
    }

    private fun measureDimension(minSize: Int, measureSpec: Int): Int {
        val sizeSpec = MeasureSpec.getSize(measureSpec)
        val modeSpec = MeasureSpec.getMode(measureSpec)

        return when(modeSpec) {
            MeasureSpec.EXACTLY -> sizeSpec
            MeasureSpec.AT_MOST -> minSize.coerceAtMost(sizeSpec)
            else -> minSize
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.strokeWidth = normalStrokeWidth
        paint.color = segmentColor
        canvas?.drawArc(arcRectF, 0F, -180F, false, paint)

        var speedCounter = 0
        var currentAngle = 0F
        for (blockIndex in 0..blockOfSegmentsAmount) {
            drawSpeedDigits(canvas, currentAngle, speedCounter)
            var segmentCounter = 0
            do {
                drawSegment(canvas, currentAngle, segmentCounter == 0)

                currentAngle += segmentsDegreeStep
                segmentCounter++
            } while (blockIndex != blockOfSegmentsAmount && segmentCounter < segmentsPerBlockAmount)

            speedCounter += speedStep
        }
        drawPointer(canvas)
    }

    private fun drawSpeedDigits(canvas: Canvas?, angle: Float, speed: Int) {
        val text = speed.toString()
        val (digitX, digitY) =
            getPointOnCircle(
                radius = arcRadius - 3 * paddingFromArc - bigSegmentLength - paint.measureText(text) / 2,
                angle = angle)

        paint.color = digitColor
        canvas?.drawText(text, digitX, digitY, paint)
    }

    private fun drawSegment(canvas: Canvas?, angle: Float, isBig: Boolean) {
        paint.strokeWidth = if (isBig) boldStrokeWidth else normalStrokeWidth

        val (fromX, fromY) =
            getPointOnCircle(
                radius = arcRadius - paddingFromArc,
                angle = angle)
        val (toX, toY) =
            getPointOnCircle(
                radius = arcRadius - paddingFromArc - if (isBig) bigSegmentLength else smallSegmentLength,
                angle = angle)

        paint.color = if (isBig) digitColor else segmentColor
        canvas?.drawLine(fromX, fromY, toX, toY, paint)
    }

    private fun drawPointer(canvas: Canvas?) {
        paint.color = pointerColor
        val currentSpeedCircleAngle = curSpeed / (maxSpeed / 180F)

        val (toX, toY) =
                getPointOnCircle(
                        radius = arcRadius - paddingFromArc,
                        angle = currentSpeedCircleAngle)

        canvas?.drawLine(arcRadius, arcRadius, toX, toY, paint)
    }

    fun setCurrentSpeed(speed: Int) {
        curSpeed = speed
        invalidate()
    }

    private fun getPointOnCircle(radius: Float, angle: Float, centreX: Float = arcRadius, centreY: Float = arcRadius) =
            Pair(centreX + radius * -cos(Math.toRadians(angle.toDouble())).toFloat(),
                    centreY + radius * -sin(Math.toRadians(angle.toDouble())).toFloat())

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putInt(STATE_MAX_SPEED, maxSpeed)
            putInt(STATE_CURRENT_SPEED, curSpeed)
            putInt(STATE_SEGMENTS_AMOUNT, segmentsPerBlockAmount)
            putInt(STATE_DIGIT_COLOR, digitColor)
            putInt(STATE_POINTER_COLOR, pointerColor)
            putInt(STATE_SEGMENT_COLOR, segmentColor)
            putParcelable(STATE_SUPER, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        state?.let{
            if (state is Bundle) {
                maxSpeed = state.getInt(STATE_MAX_SPEED)
                curSpeed = state.getInt(STATE_CURRENT_SPEED)
                segmentsPerBlockAmount = state.getInt(STATE_SEGMENTS_AMOUNT)
                digitColor = state.getInt(STATE_DIGIT_COLOR)
                pointerColor = state.getInt(STATE_POINTER_COLOR)
                segmentColor = state.getInt(STATE_SEGMENT_COLOR)
                superState = state.getParcelable(STATE_SUPER)
            }
        }
        super.onRestoreInstanceState(superState)
    }
}